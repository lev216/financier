package ru.otus.otuskotlin.financier.asset

import ru.otus.otuskotlin.financier.asset.common.persistence.IAssetRepository
import ru.otus.otuskotlin.financier.asset.config.MongoConfig
import ru.otus.otuskotlin.financier.asset.persistence.inmemory.AssetRepositoryInMemory
import ru.otus.otuskotlin.financier.asset.persistence.mongo.AssetRepositoryMongo
import kotlin.time.Duration

fun Config.getDatabaseConf(type: AssetDbType): IAssetRepository {
  val dbTypePath = "asset.service.repository.${type.confName}"
  val dbType = getProperty(dbTypePath)
  return when (dbType) {
    "in_memory" -> initInMemory()
    "mongo" -> initMongo()
    else -> throw IllegalArgumentException(
      "db type in $dbTypePath should be in_memory or mongo"
    )
  }
}

enum class AssetDbType(val confName: String) {
  PROD("prod"), TEST("test")
}

private fun Config.initInMemory(): IAssetRepository =
  AssetRepositoryInMemory(ttl = Duration.parse(getProperty("asset.service.repository.in_memory.ttl")))

private fun Config.initMongo(): IAssetRepository {
  val config = MongoConfig(this)
  return AssetRepositoryMongo(
    host = config.host,
    port = config.port,
    user = config.user,
    password = config.pass,
  )
}