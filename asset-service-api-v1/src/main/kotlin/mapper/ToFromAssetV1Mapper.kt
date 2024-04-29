package ru.otus.otuskotlin.financier.asset.api.v1.mapper

import ru.otus.otuskotlin.financier.asset.api.v1.models.*
import ru.otus.otuskotlin.financier.asset.api.v1.models.AssetType.*
import ru.otus.otuskotlin.financier.asset.common.model.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID


private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

fun mapToAsset(request: IRequest): Asset = when(request) {
    is AssetCreateRequest -> request.mapToAsset()
    is AssetUpdateRequest -> request.mapToAsset()
    else -> ASSET_NONE
}

fun mapFromAsset(asset: Asset): AssetResponseObject = when(asset) {
    is Deposit -> asset.mapToResponse()
    is Cash -> asset.mapToResponse()
    else -> throw IllegalArgumentException("Asset is unknown: $asset")
}

private fun AssetCreateRequest.mapToAsset(): Asset = when(asset.type) {
    DEPOSIT -> asset.mapToDeposit()
    CASH -> asset.mapToCash()
}

private fun AssetUpdateRequest.mapToAsset(): Asset = when(asset.type) {
    DEPOSIT -> asset.mapToDeposit()
    CASH -> asset.mapToCash()
}

private fun AssetCreateObject.mapToDeposit() = Deposit(
    id = AssetId(UUID.randomUUID().toString()),
    sum = sum,
    currency = currency,
    userId = UserId(userId),
    startDate = depositFields?.startDate.toLocalDate(),
    endDate = depositFields?.endDate.toLocalDate(),
    interestRate = depositFields?.interestRate ?:
        throw IllegalArgumentException("Deposit fields should be filled"),
)

private fun AssetUpdateObject.mapToDeposit() = Deposit(
    id = AssetId(id),
    sum = sum,
    currency = currency,
    userId = UserId(userId),
    startDate = depositFields?.startDate.toLocalDate(),
    endDate = depositFields?.endDate.toLocalDate(),
    interestRate = depositFields?.interestRate ?:
        throw IllegalArgumentException("Deposit fields should be filled"),
    lock = lock?.let { AssetLock(it) } ?: AssetLock.NONE,
)

private fun String?.toLocalDate() = LocalDate.parse(this, dateTimeFormatter)

private fun Deposit.mapToResponse() = AssetResponseObject(
    sum = sum,
    currency = currency,
    userId = userId.asString(),
    type = DEPOSIT,
    depositFields = mapDepositFields(),
    id = id.asString(),
)

private fun Deposit.mapDepositFields() = DepositFields(
    startDate = startDate.format(dateTimeFormatter),
    endDate = endDate.format(dateTimeFormatter),
    interestRate = interestRate,
)

private fun AssetCreateObject.mapToCash() = Cash(
    id = AssetId(UUID.randomUUID().toString()),
    sum = sum,
    currency = currency,
    userId = UserId(userId),
)

private fun AssetUpdateObject.mapToCash() = Cash(
    id = AssetId(id),
    sum = sum,
    currency = currency,
    userId = UserId(userId),
    lock = lock?.let { AssetLock(it) } ?: AssetLock.NONE,
)

private fun Cash.mapToResponse() = AssetResponseObject(
    sum = sum,
    currency = currency,
    userId = userId.asString(),
    type = CASH,
    id = id.asString(),
)