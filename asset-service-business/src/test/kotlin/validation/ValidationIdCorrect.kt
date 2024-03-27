package ru.otus.otuskotlin.financier.asset.business.validation

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import ru.otus.otuskotlin.financier.asset.business.processor.AssetProcessor
import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.*
import java.math.BigDecimal

fun validationIdCorrect(command: AssetCommand, processor: AssetProcessor) = runTest {
    val context = AssetContext(
        command = command,
        state = AssetState.NONE,
        workMode = AssetWorkMode.TEST,
        assetIdRequest = AssetId("da5db9d8-b13d-4094-959e-2fc57482ae70"),
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

fun validationIdTrim(command: AssetCommand, processor: AssetProcessor) = runTest {
    val context = AssetContext(
        command = command,
        state = AssetState.NONE,
        workMode = AssetWorkMode.TEST,
        assetIdRequest = AssetId("  \t  \n  da5db9d8-b13d-4094-959e-2fc57482ae70  \t  \n"),
        assetRequest = Cash(
            id = AssetId("  \t  \n  da5db9d8-b13d-4094-959e-2fc57482ae70  \t  \n"),
            sum = BigDecimal.ONE,
            currency = "USD",
            userId = UserId("userId"),
        ),
    )

    processor.exec(context)

    assertThat(context.errors).isEmpty()
    assertThat(context.state).isNotEqualTo(AssetState.FAILING)
}

fun validationIdEmpty(command: AssetCommand, processor: AssetProcessor) = runTest {
    val context = AssetContext(
        command = command,
        state = AssetState.NONE,
        workMode = AssetWorkMode.TEST,
        assetIdRequest = AssetId(""),
        assetRequest = Cash(
            id = AssetId(""),
            sum = BigDecimal.ONE,
            currency = "USD",
            userId = UserId("userId"),
        ),
    )

    processor.exec(context)

    assertThat(context.errors).isNotEmpty
    assertThat(context.errors[0].code).isEqualTo("validation-id-empty")
    assertThat(context.errors[0].field).isEqualTo("id")
    assertThat(context.state).isEqualTo(AssetState.FAILING)
}

fun validationIdFormat(command: AssetCommand, processor: AssetProcessor) = runTest {
    val context = AssetContext(
        command = command,
        state = AssetState.NONE,
        workMode = AssetWorkMode.TEST,
        assetIdRequest = AssetId("§£465@0887ygfhfir7`"),
        assetRequest = Cash(
            id = AssetId("§£465@0887ygfhfir7`"),
            sum = BigDecimal.ONE,
            currency = "USD",
            userId = UserId("userId"),
        ),
    )

    processor.exec(context)

    assertThat(context.errors).isNotEmpty
    assertThat(context.errors[0].code).isEqualTo("validation-id-badFormat")
    assertThat(context.errors[0].field).isEqualTo("id")
    assertThat(context.state).isEqualTo(AssetState.FAILING)
}