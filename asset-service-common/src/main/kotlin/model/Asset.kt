package ru.otus.otuskotlin.financier.asset.common.model

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.time.LocalDate

interface Asset {
    var id: AssetId
    val sum: BigDecimal
    val currency: String
    val userId: UserId
}

data class Cash(
    override var id: AssetId = AssetId.NONE,
    override val sum: BigDecimal = ZERO,
    override val currency: String = "RUB",
    override val userId: UserId = UserId.NONE,
) : Asset

data class Deposit(
    override var id: AssetId = AssetId.NONE,
    override val sum: BigDecimal = ZERO,
    override val currency: String = "RUB",
    override val userId: UserId = UserId.NONE,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val interestRate: BigDecimal,
) : Asset

val ASSET_NONE: Asset = Cash()