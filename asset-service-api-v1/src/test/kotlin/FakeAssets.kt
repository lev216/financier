package ru.otus.otuskotlin.financier.asset.api.v1

import ru.otus.otuskotlin.financier.asset.common.model.AssetId
import ru.otus.otuskotlin.financier.asset.common.model.Cash
import ru.otus.otuskotlin.financier.asset.common.model.Deposit
import ru.otus.otuskotlin.financier.asset.common.model.UserId
import java.math.BigDecimal.ONE
import java.math.BigDecimal.TEN
import java.time.LocalDate

object FakeAssets {
    val cash
        get() = Cash(
            AssetId("da5db9d8-b13d-4094-959e-2fc57482ae70"),
            ONE,
            "USD",
            UserId("userId"),
        )

    val deposit
        get() = Deposit(
            AssetId("da5db9d8-b13d-4094-959e-2fc57482ae70"),
            ONE,
            "USD",
            UserId("userId"),
            LocalDate.now().minusMonths(2),
            LocalDate.now().minusMonths(2).plusYears(10),
            TEN,
        )
}