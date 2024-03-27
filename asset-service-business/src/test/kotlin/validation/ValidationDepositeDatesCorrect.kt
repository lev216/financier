package ru.otus.otuskotlin.financier.asset.business.validation

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import ru.otus.otuskotlin.financier.asset.business.processor.AssetProcessor
import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.*
import java.math.BigDecimal
import java.time.LocalDate

fun validationDepositDatesCorrect(command: AssetCommand, processor: AssetProcessor) = runTest {
    val context = AssetContext(
        command = command,
        state = AssetState.NONE,
        workMode = AssetWorkMode.TEST,
        assetRequest = Deposit(
            id = AssetId("da5db9d8-b13d-4094-959e-2fc57482ae70"),
            sum = BigDecimal.ONE,
            currency = "USD",
            userId = UserId("userId"),
            startDate = LocalDate.of(2024, 1, 4),
            endDate = LocalDate.of(3034, 10, 24),
            interestRate = BigDecimal.TEN,
        ),
    )

    processor.exec(context)

    assertThat(context.errors).isEmpty()
    assertThat(context.state).isNotEqualTo(AssetState.FAILING)
}

fun validationDepositStartDateInFuture(command: AssetCommand, processor: AssetProcessor) = runTest {
    val context = AssetContext(
        command = command,
        state = AssetState.NONE,
        workMode = AssetWorkMode.TEST,
        assetRequest = Deposit(
            id = AssetId("da5db9d8-b13d-4094-959e-2fc57482ae70"),
            sum = BigDecimal.ONE,
            currency = "USD",
            userId = UserId("userId"),
            startDate = LocalDate.now().plusYears(1),
            endDate = LocalDate.of(3034, 10, 24),
            interestRate = BigDecimal.TEN,
        ),
    )

    processor.exec(context)

    assertThat(context.errors).isNotEmpty
    assertThat(context.errors[0].code).isEqualTo("validation-startDate-dateInFuture")
    assertThat(context.errors[0].field).isEqualTo("startDate")
    assertThat(context.state).isEqualTo(AssetState.FAILING)
}

fun validationDepositStartDateIsAfterEndDate(command: AssetCommand, processor: AssetProcessor) = runTest {
    val context = AssetContext(
        command = command,
        state = AssetState.NONE,
        workMode = AssetWorkMode.TEST,
        assetRequest = Deposit(
            id = AssetId("da5db9d8-b13d-4094-959e-2fc57482ae70"),
            sum = BigDecimal.ONE,
            currency = "USD",
            userId = UserId("userId"),
            startDate = LocalDate.now().minusYears(1),
            endDate = LocalDate.now().minusYears(2),
            interestRate = BigDecimal.TEN,
        ),
    )

    processor.exec(context)

    assertThat(context.errors).isNotEmpty
    assertThat(context.errors[0].code).isEqualTo("validation-endDate-dateConfusion")
    assertThat(context.errors[0].field).isEqualTo("endDate")
    assertThat(context.state).isEqualTo(AssetState.FAILING)
}

fun validationDepositEndDatePassed(command: AssetCommand, processor: AssetProcessor) = runTest {
    val context = AssetContext(
        command = command,
        state = AssetState.NONE,
        workMode = AssetWorkMode.TEST,
        assetRequest = Deposit(
            id = AssetId("da5db9d8-b13d-4094-959e-2fc57482ae70"),
            sum = BigDecimal.ONE,
            currency = "USD",
            userId = UserId("userId"),
            startDate = LocalDate.now().minusYears(1),
            endDate = LocalDate.now().minusDays(1),
            interestRate = BigDecimal.TEN,
        ),
    )

    processor.exec(context)

    assertThat(context.errors).isNotEmpty
    assertThat(context.errors[0].code).isEqualTo("validation-endDate-endDatePassed")
    assertThat(context.errors[0].field).isEqualTo("endDate")
    assertThat(context.state).isEqualTo(AssetState.FAILING)
}