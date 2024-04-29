package ru.otus.otuskotlin.financier.asset.common.model

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.time.LocalDate

interface Asset {
    var id: AssetId
    var sum: BigDecimal
    var currency: String
    var userId: UserId
    var lock: AssetLock
}

data class Cash(
    override var id: AssetId = AssetId.NONE,
    override var sum: BigDecimal = ZERO,
    override var currency: String = "RUB",
    override var userId: UserId = UserId.NONE,
    override var lock: AssetLock = AssetLock.NONE
) : Asset

data class Deposit(
    override var id: AssetId = AssetId.NONE,
    override var sum: BigDecimal = ZERO,
    override var currency: String = "RUB",
    override var userId: UserId = UserId.NONE,
    override var lock: AssetLock = AssetLock.NONE,
    var startDate: LocalDate,
    var endDate: LocalDate,
    var interestRate: BigDecimal,
) : Asset

val ASSET_NONE: Asset = Cash()