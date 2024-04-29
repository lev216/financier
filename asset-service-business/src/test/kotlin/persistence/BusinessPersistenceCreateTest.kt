package ru.otus.otuskotlin.financier.asset.business.persistence

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.Test
import ru.otus.otuskotlin.financier.asset.business.processor.AssetProcessor
import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.AssetCorSettings
import ru.otus.otuskotlin.financier.asset.common.model.*
import ru.otus.otuskotlin.financier.asset.common.persistence.DbAssetResponse
import ru.otus.otuskotlin.financier.asset.persistence.tests.AssetRepositoryMock
import java.math.BigDecimal

class BusinessPersistenceCreateTest {
    private val userId = UserId("123")
    private val command = AssetCommand.CREATE
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repository by lazy {
        AssetRepositoryMock(
            invokeCreateAsset = {
                DbAssetResponse(
                    isSuccess = true,
                    data = Cash(
                        id = AssetId(uuid),
                        userId = userId,
                        sum = it.asset.sum,
                    ),
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
    fun persistenceCreateSuccessTest() = runTest {
        val context = AssetContext(
            command = command,
            state = AssetState.NONE,
            workMode = AssetWorkMode.TEST,
            assetRequest = Cash(
                userId = userId,
                sum = BigDecimal.TEN,
            ),
        )

        processor.exec(context)

        assertThat(context.state).isEqualTo(AssetState.FINISHING)
        assertThat(context.assetResponse.id).isNotEqualTo(AssetId.NONE)
        assertThat(context.assetResponse.sum).isEqualTo(BigDecimal.TEN)
    }
}