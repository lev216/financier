package ru.otus.otuskotlin.financier.asset.stubs

import ru.otus.otuskotlin.financier.asset.common.model.*
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.time.LocalDate

object AssetStubs {
    private val CASH_STUB = Cash(
            id = AssetId("da5db9d8-b13d-4094-959e-2fc57482ae70"),
            sum = ONE,
            currency = "USD",
            userId = UserId("userId"),
        )

    private val DEPOSIT_STUB = Deposit(
            id = AssetId("da5db9d8-b13d-4094-959e-2fc57482ae70"),
            sum = ONE,
            currency = "USD",
            userId = UserId("userId"),
            startDate = LocalDate.of(2024, 1, 4),
            endDate = LocalDate.of(3034, 10, 24),
            interestRate = BigDecimal.TEN,
        )

    fun getCash() = CASH_STUB.copy()

    fun getDeposit() = DEPOSIT_STUB.copy()

    fun prepareFullSearchList() = listOf(getCash(), getDeposit())

    fun prepareEmptySearchList() = emptyList<Asset>()

}