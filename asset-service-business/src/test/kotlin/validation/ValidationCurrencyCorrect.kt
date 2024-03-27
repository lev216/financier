package ru.otus.otuskotlin.financier.asset.business.validation

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import ru.otus.otuskotlin.financier.asset.business.processor.AssetProcessor
import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.*
import java.math.BigDecimal

fun validationCurrencyCorrect(command: AssetCommand, processor: AssetProcessor) = runTest {
    val context = AssetContext(
        command = command,
        state = AssetState.NONE,
        workMode = AssetWorkMode.TEST,
        assetRequest = Cash(
            id = AssetId("da5db9d8-b13d-4094-959e-2fc57482ae70"),
            sum = BigDecimal.ONE,
            currency = "USD",
            userId = UserId("userId"),
        ),
    )

    processor.exec(context)

    assertThat(context.errors).isEmpty()
    assertThat(context.state).isNotEqualTo(AssetState.FAILING)
}

fun validationCurrencyIsEmpty(command: AssetCommand, processor: AssetProcessor) = runTest {
    val context = AssetContext(
        command = command,
        state = AssetState.NONE,
        workMode = AssetWorkMode.TEST,
        assetRequest = Cash(
            id = AssetId("da5db9d8-b13d-4094-959e-2fc57482ae70"),
            sum = BigDecimal.ONE,
            currency = "",
            userId = UserId("userId"),
        ),
    )

    processor.exec(context)

    assertThat(context.errors).isNotEmpty
    assertThat(context.errors[0].code).isEqualTo("validation-currency-empty")
    assertThat(context.errors[0].field).isEqualTo("currency")
    assertThat(context.state).isEqualTo(AssetState.FAILING)
}