package ru.otus.otuskotlin.financier.asset.business.validation

import org.junit.Test
import ru.otus.otuskotlin.financier.asset.business.processor.AssetProcessor
import ru.otus.otuskotlin.financier.asset.common.AssetCorSettings
import ru.otus.otuskotlin.financier.asset.common.model.AssetCommand
import ru.otus.otuskotlin.financier.asset.persistence.inmemory.stubs.AssetRepositoryStub

class BusinessValidationUpdateTest {

    private val command = AssetCommand.UPDATE
    private val repository = AssetRepositoryStub()
    private val settings by lazy {
        AssetCorSettings(repositoryTest = repository)
    }
    private val processor by lazy { AssetProcessor(settings) }

    @Test
    fun correctId() {
        repository.assetTypeForTest = AssetRepositoryStub.AsserType.CASH
        validationIdCorrect(command, processor)
    }

    @Test
    fun trimId() {
        repository.assetTypeForTest = AssetRepositoryStub.AsserType.CASH
        validationIdTrim(command, processor)
    }

    @Test
    fun idIsEmpty() {
        repository.assetTypeForTest = AssetRepositoryStub.AsserType.CASH
        validationIdEmpty(command, processor)
    }

    @Test
    fun idBadFormat() {
        repository.assetTypeForTest = AssetRepositoryStub.AsserType.CASH
        validationIdFormat(command, processor)
    }

    @Test
    fun correctCurrency() {
        repository.assetTypeForTest = AssetRepositoryStub.AsserType.CASH
        validationCurrencyCorrect(command, processor)
    }

    @Test
    fun currencyIsEmpty() {
        repository.assetTypeForTest = AssetRepositoryStub.AsserType.CASH
        validationCurrencyIsEmpty(command, processor)
    }

    @Test
    fun correctDepositDates() {
        repository.assetTypeForTest = AssetRepositoryStub.AsserType.DEPOSIT
        validationDepositDatesCorrect(command, processor)
    }

    @Test
    fun depositStartDateInFuture() {
        repository.assetTypeForTest = AssetRepositoryStub.AsserType.DEPOSIT
        validationDepositStartDateInFuture(command, processor)
    }

    @Test
    fun depositStartDateIsAfterEndDate() {
        repository.assetTypeForTest = AssetRepositoryStub.AsserType.DEPOSIT
        validationDepositStartDateIsAfterEndDate(command, processor)
    }

    @Test
    fun depositEndDatePassed() {
        repository.assetTypeForTest = AssetRepositoryStub.AsserType.DEPOSIT
        validationDepositEndDatePassed(command, processor)
    }

    @Test
    fun correctInterestRate() {
        repository.assetTypeForTest = AssetRepositoryStub.AsserType.DEPOSIT
        validationInterestRateCorrect(command, processor)
    }

    @Test
    fun interestRateNegative() {
        repository.assetTypeForTest = AssetRepositoryStub.AsserType.DEPOSIT
        validationInterestRateNegative(command, processor)
    }

    @Test
    fun correctSum() {
        repository.assetTypeForTest = AssetRepositoryStub.AsserType.CASH
        validationSumCorrect(command, processor)
    }

    @Test
    fun sumNegative() {
        repository.assetTypeForTest = AssetRepositoryStub.AsserType.CASH
        validationSumNegative(command, processor)
    }
}