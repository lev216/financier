package ru.otus.otuskotlin.financier.asset.api.v1

import ru.otus.otuskotlin.financier.asset.common.model.Cash
import ru.otus.otuskotlin.financier.asset.common.model.Deposit
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.BigDecimal.TEN
import java.time.LocalDateTime

object FakeAssets {
    val cash
        get() = Cash(
            "da5db9d8-b13d-4094-959e-2fc57482ae70",
            ONE,
            "USD",
            "userId",
        )

    val deposit
        get() = Deposit(
            "da5db9d8-b13d-4094-959e-2fc57482ae70",
            ONE,
            "USD",
            "userId",
            LocalDateTime.of(2024, 1, 4, 19, 27),
            LocalDateTime.of(3034, 10, 24, 9, 7),
            TEN,
        )
}