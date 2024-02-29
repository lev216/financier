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
    AssetId(UUID.randomUUID().toString()),
    sum,
    currency,
    UserId(userId),
    depositFields?.startDate.toLocalDate(),
    depositFields?.endDate.toLocalDate(),
    depositFields?.interestRate ?:
        throw IllegalArgumentException("Deposit fields should be filled"),
)

private fun AssetUpdateObject.mapToDeposit() = Deposit(
    AssetId(id),
    sum,
    currency,
    UserId(userId),
    depositFields?.startDate.toLocalDate(),
    depositFields?.endDate.toLocalDate(),
    depositFields?.interestRate ?:
        throw IllegalArgumentException("Deposit fields should be filled"),
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
    startDate.format(dateTimeFormatter),
    endDate.format(dateTimeFormatter),
    interestRate,
)

private fun AssetCreateObject.mapToCash() = Cash(
    AssetId(UUID.randomUUID().toString()),
    sum,
    currency,
    UserId(userId),
)

private fun AssetUpdateObject.mapToCash() = Cash(
    AssetId(id),
    sum,
    currency,
    UserId(userId),
)

private fun Cash.mapToResponse() = AssetResponseObject(
    sum = sum,
    currency = currency,
    userId = userId.asString(),
    type = CASH,
    id = id.asString(),
)