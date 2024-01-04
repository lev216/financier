package ru.otus.otuskotlin.financier.asset.common.model

import java.math.BigDecimal
import java.time.LocalDateTime

interface Asset {
    val id: String
    val sum: BigDecimal
    val currency: String
    val userId: String
}

data class Cash(
    override val id: String,
    override val sum: BigDecimal,
    override val currency: String,
    override val userId: String,
) : Asset

data class Deposit(
    override val id: String,
    override val sum: BigDecimal,
    override val currency: String,
    override val userId: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val interestRate: BigDecimal,
) : Asset