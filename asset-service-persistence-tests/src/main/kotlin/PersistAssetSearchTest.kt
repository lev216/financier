package ru.otus.otuskotlin.financier.asset.persistence.tests

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import ru.otus.otuskotlin.financier.asset.common.model.Asset
import ru.otus.otuskotlin.financier.asset.common.model.UserId
import ru.otus.otuskotlin.financier.asset.common.persistence.DbAssetFilterRequest
import ru.otus.otuskotlin.financier.asset.common.persistence.IAssetRepository

abstract class PersistAssetSearchTest {
    abstract val repository: IAssetRepository

    protected open val initializedObjects: List<Asset> = initObjects

    @Test
    fun searchUser() = runPersistenceTest {
        val result = repository.searchAsset(DbAssetFilterRequest(userId = searchUserId))

        with(result) {
            assertThat(isSuccess).isTrue()
            assertThat(data).containsExactlyInAnyOrder(initializedObjects[1], initializedObjects[3])
            assertThat(errors).isEmpty()
        }
    }

    companion object : BaseInitAssets("search") {
        private val searchUserId = UserId("user-1234")

        override val initObjects: List<Asset> = listOf(
            createInitTestModel("asset1"),
            createInitTestModel("asset2", userId = searchUserId),
            createInitTestModel("asset3"),
            createInitTestModel("asset4", userId = searchUserId),
            createInitTestModel("asset5"),
        )
    }
}