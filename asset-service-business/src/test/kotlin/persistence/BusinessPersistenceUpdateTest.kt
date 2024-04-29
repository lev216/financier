package ru.otus.otuskotlin.financier.asset.business.persistence

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import ru.otus.otuskotlin.financier.asset.business.processor.AssetProcessor
import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.AssetCorSettings
import ru.otus.otuskotlin.financier.asset.common.model.*
import ru.otus.otuskotlin.financier.asset.common.persistence.DbAssetResponse
import ru.otus.otuskotlin.financier.asset.persistence.tests.AssetRepositoryMock
import java.math.BigDecimal

class BusinessPersistenceUpdateTest {
    private val userId = UserId("123")
    private val command = AssetCommand.UPDATE
    private val initAsset = Cash(
        id = AssetId("123"),
        userId = userId,
        sum = BigDecimal.TEN,
    )
    private val repository by lazy {
        AssetRepositoryMock(
            invokeReadAsset = {
                DbAssetResponse(
                    isSuccess = true,
                    data = initAsset,
                )
            },
            invokeUpdateAsset = {
                DbAssetResponse(
                    isSuccess = true,
                    data = Cash(
                        id = AssetId("123"),
                        userId = userId,
                        sum = BigDecimal.ONE,
                    )
                )
            }
        )
    }
    private val settings by lazy {
        AssetCorSettings(
            repositoryTest = repository,
        )
    }
    private val processor by lazy { AssetProcessor(settings) }

    @Test
    fun persistenceUpdateSuccessTest() = runTest {
        val context = AssetContext(
            command = command,
            state = AssetState.NONE,
            workMode = AssetWorkMode.TEST,
            assetRequest = Cash(
                id = AssetId("123"),
                userId = userId,
                sum = BigDecimal.ONE,
            ),
        )

        processor.exec(context)

        assertThat(context.state).isEqualTo(AssetState.FINISHING)
        assertThat(context.assetResponse.id).isEqualTo(initAsset.id)
        assertThat(context.assetResponse.sum).isEqualTo(BigDecimal.ONE)
        assertThat(context.assetResponse.userId).isEqualTo(initAsset.userId)
    }

    @Test
    fun persistenceReadNotFoundTest() = notFoundTest(command)
}