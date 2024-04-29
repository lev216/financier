package ru.otus.otuskotlin.financier.asset.persistence.mongo

import org.testcontainers.containers.GenericContainer
import ru.otus.otuskotlin.financier.asset.common.model.Asset
import ru.otus.otuskotlin.financier.asset.common.model.AssetLock
import ru.otus.otuskotlin.financier.asset.common.persistence.IAssetRepository
import ru.otus.otuskotlin.financier.asset.persistence.tests.*
import java.time.Duration

class PersistAssetMongoCreateTest : PersistAssetCreateTest() {
  override val repository: IAssetRepository = TestCompanion.repository(initObjects, lockNew)
}

class PersistAssetMongoDeleteTest : PersistAssetDeleteTest() {
  override val repository: IAssetRepository = TestCompanion.repository(initObjects, lockOld)
}

class PersistAssetMongoReadTest : PersistAssetReadTest() {
  override val repository: IAssetRepository = TestCompanion.repository(initObjects, AssetLock(""))
}

class PersistAssetMongoSearchTest : PersistAssetSearchTest() {
  override val repository: IAssetRepository = TestCompanion.repository(initObjects, AssetLock(""))
}

class PersistAssetMongoUpdateTest : PersistAssetUpdateTest() {
  override val repository: IAssetRepository = TestCompanion.repository(initObjects, lockNew)
}

class TestMongoContainer : GenericContainer<TestMongoContainer>("mongo:5")

object TestCompanion {
  private val container by lazy {
    TestMongoContainer().withStartupTimeout(Duration.ofSeconds(300))
      .withEnv("MONGO_INITDB_ROOT_USERNAME", "mongo")
      .withEnv("MONGO_INITDB_ROOT_PASSWORD", "mongo")
      .withEnv("MONGO_INITDB_DATABASE", "assets")
      .withExposedPorts(27017)
      .also { it.start() }
  }

  fun repository(initObjects: List<Asset>, lock: AssetLock) = AssetRepositoryMongo(
    host = container.host,
    port = container.firstMappedPort,
    testing = true,
    randomUuid = { lock.asString() },
    initObjects = initObjects,
  )
}