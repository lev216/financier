package ru.otus.otuskotlin.financier.asset.persistence.mongo

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.financier.asset.common.helper.asAssetError
import ru.otus.otuskotlin.financier.asset.common.model.*
import ru.otus.otuskotlin.financier.asset.common.persistence.*
import ru.otus.otuskotlin.financier.asset.persistence.mongo.model.AssetEntity
import ru.otus.otuskotlin.financier.asset.persistence.mongo.model.AssetType
import java.util.UUID

class AssetRepositoryMongo(
  host: String = "localhost",
  port: Int = 27017,
  user: String = "mongo",
  password: String = "mongo",
  private val randomUuid: () -> String = { UUID.randomUUID().toString() },
  private val initObjects: Collection<Asset> = emptyList(),
  testing: Boolean = false,
) : IAssetRepository {

  private val logger = LoggerFactory.getLogger(AssetRepositoryMongo::class.java)

  private companion object {
    const val ASSETS_COLLECTION = "assets"
    val ID_IS_EMPTY = DbAssetResponse.error(AssetError(field = "id", message = "Id is empty"))
    val ID_NOT_FOUND = DbAssetResponse.error(AssetError(field = "id", code = "not-found", message = "Not Found"))
    val CONCURRENT_MODIFICATION = DbAssetResponse.error(AssetError(field = "lock", code = "concurrency", message = "Concurrent modification"))
  }

  private val mongoClient =
    MongoClient.create("mongodb://$user:$password@$host:$port")

  private val database =
    mongoClient.getDatabase("assets")

  init {
    if (testing) {
      runBlocking {
        database.createCollection(collectionName = ASSETS_COLLECTION)
        initObjects.map { it.toEntity() }.forEach { database.getCollection<AssetEntity>(ASSETS_COLLECTION).insertOne(it) }
      }
    }
  }


  override suspend fun createAsset(request: DbAssetRequest): DbAssetResponse {
    val asset = request.asset.copy().also {
      it.id = AssetId(randomUuid())
      it.lock = AssetLock(randomUuid())
    }
    return doDbAction(
      "create",
      {
        database.getCollection<AssetEntity>(ASSETS_COLLECTION).insertOne(asset.toEntity())
        DbAssetResponse.success(asset)
      },
      ::errorToAssetResponse,
    )
  }

  override suspend fun readAsset(request: DbAssetIdRequest): DbAssetResponse =
    if (request.id == AssetId.NONE) {
      ID_IS_EMPTY
    } else {
      doDbAction(
        "read",
        {
          val asset = database.getCollection<AssetEntity>(ASSETS_COLLECTION).withDocumentClass<AssetEntity>()
            .find(Filters.eq("_id", request.id.asString()))
            .firstOrNull()?.toAsset()
          asset?.let { DbAssetResponse.success(it) } ?: ID_NOT_FOUND
        },
        ::errorToAssetResponse,
      )
    }


  override suspend fun updateAsset(request: DbAssetRequest): DbAssetResponse = doDbAction(
      "update",
      {
        with(request.asset) {
          val prevLock = lock
          val readAsset = readAsset(DbAssetIdRequest(this))
          if (!readAsset.isSuccess) {
            return@doDbAction readAsset
          } else if (prevLock != readAsset.data!!.lock) {
            return@doDbAction CONCURRENT_MODIFICATION
          }
          val query = Filters.eq("_id", id.asString())
          val updates = when (this) {
            is Cash -> Updates.combine(
              Updates.set(AssetEntity::sum.name, sum),
              Updates.set(AssetEntity::currency.name, currency),
              Updates.set(AssetEntity::lock.name, randomUuid()),
            )

            is Deposit -> Updates.combine(
              Updates.set(AssetEntity::sum.name, sum),
              Updates.set(AssetEntity::currency.name, currency),
              Updates.set(AssetEntity::startDate.name, startDate),
              Updates.set(AssetEntity::endDate.name, endDate),
              Updates.set(AssetEntity::interestRate.name, interestRate),
              Updates.set(AssetEntity::lock.name, randomUuid()),
            )

            else -> throw Exception("Asset's type $javaClass is unknown")
          }
          val result = database.getCollection<AssetEntity>(ASSETS_COLLECTION)
            .updateOne(query, updates).modifiedCount
          if (result > 0)
            DbAssetResponse.success(request.asset)
          else
            ID_NOT_FOUND
        }
      },
      ::errorToAssetResponse,
    )

  override suspend fun deleteAsset(request: DbAssetIdRequest): DbAssetResponse = doDbAction(
      "delete",
      {
        val readAssetResult = readAsset(request)
        if (!readAssetResult.isSuccess) {
          readAssetResult
        } else if (request.lock != readAssetResult.data!!.lock) {
          CONCURRENT_MODIFICATION
        } else {
          val result = database.getCollection<AssetEntity>(ASSETS_COLLECTION)
            .deleteOne(Filters.eq("_id", request.id.asString()))
            .deletedCount
          if (result > 0)
            DbAssetResponse.success(readAssetResult.data!!)
          else
            CONCURRENT_MODIFICATION
        }
      },
      ::errorToAssetResponse,
    )

  override suspend fun searchAsset(request: DbAssetFilterRequest): DbAssetsResponse = doDbAction(
      "search",
      {
        val filter = when(request.type) {
          AssetSearchType.NONE -> Filters.eq(AssetEntity::userId.name, request.userId.asString())
          AssetSearchType.DEPOSIT -> Filters.and(
            Filters.eq(AssetEntity::type.name, AssetType.DEPOSIT),
            Filters.gte(AssetEntity::startDate.name, request.startDate),
            Filters.lte(AssetEntity::endDate.name, request.endDate),
            Filters.eq(AssetEntity::userId.name, request.userId.asString()),
          )
          AssetSearchType.CASH -> Filters.and(
            Filters.eq(AssetEntity::type.name, AssetType.CASH),
            Filters.eq(AssetEntity::userId.name, request.userId.asString()),
          )
        }
        val assets = database.getCollection<AssetEntity>(ASSETS_COLLECTION).withDocumentClass<AssetEntity>()
          .find(filter)
          .map { it.toAsset() }
          .toList()
        DbAssetsResponse.success(assets)
      },
      ::errorToAssetsResponse,
    )

  private fun errorToAssetResponse(e: Exception) = DbAssetResponse.error(e.asAssetError())

  private fun errorToAssetsResponse(e: Exception) = DbAssetsResponse.error(e.asAssetError())

  private inline fun <Response> doDbAction(
    name: String,
    dbAction: () -> Response,
    errorToResponse: Exception.() -> Response,
  ): Response = try {
    dbAction()
  } catch (e: Exception) {
    logger.error("Failed to $name", e)
    errorToResponse(e)
  }

  private fun Asset.copy() = when(this) {
    is Cash -> this.copy()
    is Deposit -> this.copy()
    else -> throw Exception("Type ${this::class.java} is unknown")
  }
}