package ru.otus.otuskotlin.financier.asset.persistence.tests

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import ru.otus.otuskotlin.financier.asset.common.model.Asset
import ru.otus.otuskotlin.financier.asset.common.model.AssetId
import ru.otus.otuskotlin.financier.asset.common.persistence.DbAssetIdRequest
import ru.otus.otuskotlin.financier.asset.common.persistence.IAssetRepository

abstract class PersistAssetReadTest {
    abstract val repository: IAssetRepository
    protected open val readSuccess = initObjects[0]

    @Test
    fun readSuccess() = runPersistenceTest {
        val result = repository.readAsset(DbAssetIdRequest(readSuccess.id))

        with(result) {
            assertThat(isSuccess).isTrue()
            assertThat(data).isEqualTo(readSuccess)
            assertThat(errors).isEmpty()
        }
    }

    @Test
    fun readNotFound() = runPersistenceTest {
        val result = repository.readAsset(DbAssetIdRequest(notFoundId))

        with(result) {
            assertThat(isSuccess).isFalse()
            assertThat(data).isNull()
            assertThat(errors.find { it.code == "not-found" }?.field).isEqualTo("id")
        }
    }

    companion object : BaseInitAssets("read") {
        override val initObjects: List<Asset> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = AssetId("asset-read-not-found")
    }
}