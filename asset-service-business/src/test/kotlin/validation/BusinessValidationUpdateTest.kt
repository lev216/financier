package ru.otus.otuskotlin.financier.asset.business.validation

import org.junit.Test
import ru.otus.otuskotlin.financier.asset.business.processor.AssetProcessor
import ru.otus.otuskotlin.financier.asset.common.model.AssetCommand

class BusinessValidationUpdateTest {

    private val command = AssetCommand.UPDATE
    private val processor by lazy { AssetProcessor() }

    @Test
    fun correctId() = validationIdCorrect(command, processor)

    @Test
    fun trimId() = validationIdTrim(command, processor)

    @Test
    fun idIsEmpty() = validationIdEmpty(command, processor)

    @Test
    fun idBadFormat() = validationIdFormat(command, processor)

    @Test
    fun correctCurrency() = validationCurrencyCorrect(command, processor)

    @Test
    fun currencyIsEmpty() = validationCurrencyIsEmpty(command, processor)

    @Test
    fun correctDepositDates() = validationDepositDatesCorrect(command, processor)

    @Test
    fun depositStartDateInFuture() = validationDepositStartDateInFuture(command, processor)

    @Test
    fun depositStartDateIsAfterEndDate() = validationDepositStartDateIsAfterEndDate(command, processor)

    @Test
    fun depositEndDatePassed() = validationDepositEndDatePassed(command, processor)

    @Test
    fun correctInterestRate() = validationInterestRateCorrect(command, processor)

    @Test
    fun interestRateNegative() = validationInterestRateNegative(command, processor)

    @Test
    fun correctSum() = validationSumCorrect(command, processor)

    @Test
    fun sumNegative() = validationSumNegative(command, processor)

}