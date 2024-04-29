package ru.otus.otuskotlin.financier.asset.persistence.tests

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import ru.otus.otuskotlin.financier.asset.common.model.*
import ru.otus.otuskotlin.financier.asset.common.persistence.DbAssetRequest
import ru.otus.otuskotlin.financier.asset.common.persistence.IAssetRepository
import java.math.BigDecimal

abstract class PersistAssetCreateTest {
    abstract val repository: IAssetRepository

    protected open val lockNew: AssetLock = AssetLock("20000000-0000-0000-0000-000000000002")

    private val createCash = Cash(
        sum = BigDecimal.TEN,
        userId = UserId("user-123")
    )

    @Test
    fun createSuccess() = runPersistenceTest {
        val result = repository.createAsset(DbAssetRequest(createCash))
        val expected = createCash.copy(id = result.data?.id ?: AssetId.NONE)
        with(result) {
            assertThat(isSuccess).isTrue()
            assertThat(data?.id).isEqualTo(expected.id)
            assertThat(data?.lock).isEqualTo(lockNew)
            assertThat(data?.sum).isEqualTo(expected.sum)
            assertThat(data?.userId).isEqualTo(expected.userId)
            assertThat(data?.currency).isEqualTo(expected.currency)
        }
    }

    companion object : BaseInitAssets("create") {
        override val initObjects: List<Asset> = emptyList()
    }
}