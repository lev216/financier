package ru.otus.otuskotlin.financier.asset.persistence.inmemory

import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.otus.otuskotlin.financier.asset.common.model.*
import ru.otus.otuskotlin.financier.asset.common.persistence.*
import ru.otus.otuskotlin.financier.asset.common.persistence.DbAssetResponse.Companion.success
import ru.otus.otuskotlin.financier.asset.persistence.inmemory.model.AssetEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class AssetRepositoryInMemory(
    initObjects: Collection<Asset> = emptyList(),
    ttl: Duration = 2.minutes,
    private val randomUuid: () -> String = { UUID.randomUUID().toString() },
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
) : IAssetRepository {

    private val cache = Cache.Builder<String, AssetEntity>()
        .expireAfterWrite(ttl)
        .build()
    private val mutex: Mutex = Mutex()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(asset: Asset) {
        val entity = asset.toEntity()
        cache.put(entity.id, entity)
    }

    override suspend fun createAsset(request: DbAssetRequest): DbAssetResponse {
        val key = randomUuid()
        val asset = when (request.asset) {
            is Cash -> (request.asset as Cash).copy(id = AssetId(key), lock = AssetLock(randomUuid()))
            is Deposit -> (request.asset as Deposit).copy(id = AssetId(key), lock = AssetLock(randomUuid()))
            else -> throw IllegalArgumentException("Asset ${request.asset::class.java} is unknown")
        }
        val entity = asset.toEntity()
        cache.put(key, entity)
        return DbAssetResponse(
            data = asset,
            isSuccess = true,
        )
    }

    override suspend fun readAsset(request: DbAssetIdRequest): DbAssetResponse {
        val key = request.id.takeIf { it != AssetId.NONE }?.asString() ?: return resultErrorEmptyId
        return cache.get(key)
            ?.let {
                DbAssetResponse(
                    data = it.toAsset(),
                    isSuccess = true,
                )
            } ?: resultErrorNotFound
    }

    private suspend fun doUpdate(
        id: AssetId,
        oldLock: AssetLock,
        okBlock: (key: String, oldAsset: AssetEntity) -> DbAssetResponse
    ): DbAssetResponse {
        val key = id.takeIf { it != AssetId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLockString = oldLock.takeIf { it != AssetLock.NONE }?.asString()
            ?: return resultErrorEmptyLock

        return mutex.withLock {
            val oldAsset = cache.get(key)
            when {
                oldAsset == null -> return resultErrorNotFound
                oldAsset.lock != oldLockString -> DbAssetResponse.errorConcurrent(
                    oldLock,
                    oldAsset.toAsset()
                )
                else -> okBlock(key, oldAsset)
            }
        }
    }

    override suspend fun updateAsset(request: DbAssetRequest): DbAssetResponse =
        doUpdate(request.asset.id, request.asset.lock) { key, _ ->
            val newAsset = when (request.asset) {
                is Cash -> (request.asset as Cash).copy(lock = AssetLock(randomUuid()))
                is Deposit -> (request.asset as Deposit).copy(lock = AssetLock(randomUuid()))
                else -> throw IllegalArgumentException("Asset ${request.asset::class.java} is unknown")
            }
            val entity = newAsset.toEntity()
            cache.put(key, entity)
            success(newAsset)
        }

    override suspend fun deleteAsset(request: DbAssetIdRequest): DbAssetResponse =
        doUpdate(request.id, request.lock) { key, oldAssetEntity ->
            cache.invalidate(key)
            success(oldAssetEntity.toAsset())
        }

    override suspend fun searchAsset(request: DbAssetFilterRequest): DbAssetsResponse {
        val result = cache.asMap().asSequence()
            .filter { entry ->
                request.userId.takeIf { it != UserId.NONE }?.let {
                    it.asString() == entry.value.userId
                } ?: true
            }
            .filter { entry ->
                request.startDate.takeIf { it != LocalDate.MIN }?.let {
                    val startDate = entry.value.startDate?.run {
                        LocalDate.parse(entry.value.startDate, dateTimeFormatter)
                    } ?: LocalDate.MIN
                    startDate.isAfter(it)
                } ?: true
            }
            .filter { entry ->
                request.endDate.takeIf { it != LocalDate.MAX }?.let {
                    val endDate = entry.value.endDate?.run {
                        LocalDate.parse(entry.value.endDate, dateTimeFormatter)
                    } ?: LocalDate.MAX
                    endDate.isBefore(it)
                } ?: true
            }
            .filter { entry ->
                request.type.takeIf { it != AssetSearchType.NONE }?.let {
                    it.toString() == entry.value.type.toString()
                } ?: true
            }
            .map { it.value.toAsset() }
            .toList()
        return DbAssetsResponse(
            data = result,
            isSuccess = true
        )
    }

    companion object {
        val resultErrorEmptyId = DbAssetResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                AssetError(
                    code = "id-empty",
                    group = "validation",
                    field = "id",
                    message = "Id must not be null or blank",
                )
            )
        )
        val resultErrorEmptyLock = DbAssetResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                AssetError(
                    code = "lock-empty",
                    group = "validation",
                    field = "lock",
                    message = "Lock must not be null or blank",
                )
            )
        )
        val resultErrorNotFound = DbAssetResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                AssetError(
                    code = "not-found",
                    field = "id",
                    message = "Not Found",
                )
            )
        )
    }

}