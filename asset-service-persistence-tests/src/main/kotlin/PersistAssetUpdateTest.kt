package ru.otus.otuskotlin.financier.asset.persistence.tests

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import ru.otus.otuskotlin.financier.asset.common.model.*
import ru.otus.otuskotlin.financier.asset.common.persistence.DbAssetRequest
import ru.otus.otuskotlin.financier.asset.common.persistence.IAssetRepository
import java.math.BigDecimal

abstract class PersistAssetUpdateTest {
    abstract val repository: IAssetRepository
    protected open val updateSuccess = initObjects[0]
    protected open val updateConcurrency = initObjects[1]
    protected val updateIdNotFound = AssetId("asset-update-not-found")
    protected val lockBad = AssetLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = AssetLock("20000000-0000-0000-0000-000000000002")

    private val updateCashSuccess by lazy {
        Cash(
            id = updateSuccess.id,
            sum = BigDecimal.TEN,
            userId = UserId("user-123"),
            lock = initObjects[0].lock,
        )
    }

    private val updateCashNotFound by lazy {
        Cash(
            id = updateIdNotFound,
            sum = BigDecimal.TEN,
            userId = UserId("user-123"),
            lock = lockBad,
        )
    }

    private val updateCashConcurrency by lazy {
        Cash(
            id = updateConcurrency.id,
            sum = BigDecimal.TEN,
            userId = UserId("user-123"),
            lock = lockBad,
        )
    }

    @Test
    fun updateSuccess() = runPersistenceTest {
        val result = repository.updateAsset(DbAssetRequest(updateCashSuccess))
        with(result) {
            assertThat(isSuccess).isTrue()
            assertThat(data?.id).isEqualTo(updateCashSuccess.id)
            assertThat(data?.sum).isEqualTo(updateCashSuccess.sum)
            assertThat(data?.currency).isEqualTo(updateCashSuccess.currency)
            assertThat(data?.userId).isEqualTo(updateCashSuccess.userId)
            assertThat(data?.lock).isEqualTo(lockNew)
            assertThat(errors).isEmpty()
        }
    }

    @Test
    fun updateNotFound() = runPersistenceTest {
        val result = repository.updateAsset(DbAssetRequest(updateCashNotFound))
        with(result) {
            assertThat(isSuccess).isFalse()
            assertThat(data).isNull()
            assertThat(errors.find { it.code == "not-found" }?.field).isEqualTo("id")
        }
    }

    @Test
    fun updateConcurrencyError() = runPersistenceTest {
        val result = repository.updateAsset(DbAssetRequest(updateCashConcurrency))
        with(result) {
            assertThat(isSuccess).isFalse()
            assertThat(errors.find { it.code == "concurrency" }?.field).isEqualTo("lock")
        }
    }

    companion object : BaseInitAssets("update") {
        override val initObjects: List<Asset> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConcurrent"),
        )
    }
}