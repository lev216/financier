package ru.otus.otuskotlin.financier.asset.business.persistence

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import ru.otus.otuskotlin.financier.asset.business.processor.AssetProcessor
import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.AssetCorSettings
import ru.otus.otuskotlin.financier.asset.common.model.*
import ru.otus.otuskotlin.financier.asset.common.persistence.DbAssetResponse
import ru.otus.otuskotlin.financier.asset.persistence.tests.AssetRepositoryMock
import java.math.BigDecimal

private val initCash = Cash(
    id = AssetId("123"),
    userId = UserId("123"),
    sum = BigDecimal.TEN,
)
private val repository = AssetRepositoryMock(
    invokeReadAsset = {
        if (initCash.id == it.id) {
            DbAssetResponse(
                isSuccess = true,
                data = initCash,
            )
        } else {
            DbAssetResponse(
                isSuccess = false,
                data = null,
                errors = listOf(AssetError(message = "Not found", field = "id")),
            )
        }
    }
)
private val settings by lazy {
    AssetCorSettings(
        repositoryTest = repository,
    )
}
private val processor by lazy { AssetProcessor(settings) }

fun notFoundTest(command: AssetCommand) = runTest {
    val context = AssetContext(
        command = command,
        state = AssetState.NONE,
        workMode = AssetWorkMode.TEST,
        assetRequest = Cash(
            id = AssetId("12345"),
            sum = BigDecimal.TEN,
            userId = UserId("123"),
            lock = AssetLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(context)
    assertThat(context.state).isEqualTo(AssetState.FAILING)
    assertThat(context.assetResponse).isEqualTo(Cash())
    assertThat(context.errors.size).isEqualTo(1)
    assertThat(context.errors.first().field).isEqualTo("id")
}