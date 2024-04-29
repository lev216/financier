package ru.otus.otuskotlin.financier.asset.business.persistence

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import ru.otus.otuskotlin.financier.asset.business.processor.AssetProcessor
import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.AssetCorSettings
import ru.otus.otuskotlin.financier.asset.common.model.*
import ru.otus.otuskotlin.financier.asset.common.persistence.DbAssetsResponse
import ru.otus.otuskotlin.financier.asset.persistence.tests.AssetRepositoryMock
import java.math.BigDecimal

class BusinessPersistenceSearchTest {
    private val userId = UserId("123")
    private val command = AssetCommand.SEARCH
    private val initAsset = Cash(
        id = AssetId("123"),
        userId = userId,
        sum = BigDecimal.TEN,
    )
    private val repository by lazy {
        AssetRepositoryMock(
            invokeSearchAssets = {
                DbAssetsResponse(
                    isSuccess = true,
                    data = listOf(initAsset)
                )
            },
        )
    }

    private val settings by lazy {
        AssetCorSettings(
            repositoryTest = repository,
        )
    }
    private val processor by lazy {
        AssetProcessor(
            corSettings = settings,
        )
    }

    @Test
    fun persistenceSearchSuccessTest() = runTest {
        val context = AssetContext(
            command = command,
            state = AssetState.NONE,
            workMode = AssetWorkMode.TEST,
            assetFilterRequest = AssetFilter(
                userId = userId
            ),
        )

        processor.exec(context)

        assertThat(context.state).isEqualTo(AssetState.FINISHING)
        assertThat(context.assetsResponse.size).isEqualTo(1)
    }
}