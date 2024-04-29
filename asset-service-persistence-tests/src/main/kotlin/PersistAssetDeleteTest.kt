package ru.otus.otuskotlin.financier.asset.persistence.tests

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import ru.otus.otuskotlin.financier.asset.common.model.Asset
import ru.otus.otuskotlin.financier.asset.common.model.AssetId
import ru.otus.otuskotlin.financier.asset.common.persistence.DbAssetIdRequest
import ru.otus.otuskotlin.financier.asset.common.persistence.IAssetRepository

abstract class PersistAssetDeleteTest {
    abstract val repository: IAssetRepository
    protected open val deleteSuccess = initObjects[0]
    protected open val deleteConcurrency = initObjects[1]
    protected open val notFoundId = AssetId("asset-delete-not-found")

    @Test
    fun deleteSuccess() = runPersistenceTest {
        val lockOld = deleteSuccess.lock
        val result = repository.deleteAsset(DbAssetIdRequest(deleteSuccess.id, lockOld))

        with(result) {
            assertThat(isSuccess).isTrue()
            assertThat(errors).isEmpty()
            assertThat(data?.lock).isEqualTo(lockOld)
        }
    }

    @Test
    fun deleteNotFound() = runPersistenceTest {
        val result = repository.deleteAsset(DbAssetIdRequest(notFoundId, lockOld))

        with(result) {
            assertThat(isSuccess).isFalse()
            assertThat(data).isNull()
            assertThat(errors.find { it.code == "not-found" }?.field).isEqualTo("id")
        }
    }

    @Test
    fun deleteConcurrency() = runPersistenceTest {
        val result = repository.deleteAsset(DbAssetIdRequest(deleteConcurrency.id, lockBad))

        with(result) {
            assertThat(isSuccess).isFalse()
            assertThat(errors.find { it.code == "concurrency" }?.field).isEqualTo("lock")
        }
    }

    companion object : BaseInitAssets("delete") {
        override val initObjects: List<Asset> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
    }
}