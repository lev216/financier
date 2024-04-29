package ru.otus.otuskotlin.financier.asset.persistence.inmemory

import ru.otus.otuskotlin.financier.asset.common.model.*
import ru.otus.otuskotlin.financier.asset.persistence.inmemory.model.AssetEntity
import ru.otus.otuskotlin.financier.asset.persistence.inmemory.model.AssetType
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val DATE_FORMAT = "dd/mm/yyyy"
private val dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT)

fun Asset.toEntity() = when(this) {
    is Cash -> AssetEntity(
        id = id.asString(),
        type = AssetType.CASH,
        sum = sum,
        currency = currency,
        userId = userId.asString(),
        lock = lock.asString(),
    )
    is Deposit -> AssetEntity(
        id = id.asString(),
        type = AssetType.DEPOSIT,
        sum = sum,
        currency = currency,
        userId = userId.asString(),
        lock = lock.asString(),
        startDate = dateTimeFormatter.format(startDate),
        endDate = dateTimeFormatter.format(endDate),
        interestRate = interestRate,
    )
    else -> throw IllegalArgumentException("Asset ${this::class.java} is unknown for persistence")
}

fun AssetEntity.toAsset() = when(this.type) {
    AssetType.CASH -> Cash(
        id = AssetId(id),
        sum = sum,
        currency = currency,
        userId = UserId(userId),
        lock = lock?.let { AssetLock(lock) } ?: AssetLock.NONE,
    )
    AssetType.DEPOSIT -> Deposit(
        id = AssetId(id),
        sum = sum,
        currency = currency,
        userId = UserId(userId),
        lock = lock?.let { AssetLock(lock) } ?: AssetLock.NONE,
        startDate = LocalDate.parse(startDate, dateTimeFormatter),
        endDate = LocalDate.parse(endDate, dateTimeFormatter),
        interestRate = interestRate!!,
    )
}