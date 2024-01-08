package ru.otus.otuskotlin.financier.asset.mapper

import ru.otus.otuskotlin.financier.asset.api.v1.models.*
import ru.otus.otuskotlin.financier.asset.model.Asset
import ru.otus.otuskotlin.financier.asset.model.Cash
import ru.otus.otuskotlin.financier.asset.model.Deposit
import java.time.LocalDateTime
import java.util.UUID

fun mapToAsset(request: IRequest): Asset = when(request) {
    is AssetCreateRequest -> request.mapToAsset()
    is AssetUpdateRequest -> request.mapToAsset()
    else -> throw IllegalArgumentException("Asset can't be mapped from request: $request")
}

fun mapFromAsset(asset: Asset): AssetResponseObject = when(asset) {
    is Deposit -> asset.mapToResponse()
    is Cash -> asset.mapToResponse()
    else -> throw IllegalArgumentException("Asset is unknown: $asset")
}

private fun AssetCreateRequest.mapToAsset(): Asset = when(asset.type) {
    AssetCreateObject.Type.DEPOSIT -> asset.mapToDeposit()
    AssetCreateObject.Type.CASH -> asset.mapToCash()
}

private fun AssetUpdateRequest.mapToAsset(): Asset = when(asset.type) {
    AssetUpdateObject.Type.DEPOSIT -> asset.mapToDeposit()
    AssetUpdateObject.Type.CASH -> asset.mapToCash()
}

private fun AssetCreateObject.mapToDeposit() = Deposit(
    UUID.randomUUID().toString(),
    sum,
    currency,
    userId,
    LocalDateTime.parse(depositFields?.startDate),
    LocalDateTime.parse(depositFields?.endDate),
    depositFields?.interestRate ?: throw IllegalArgumentException("Deposit fields should be filled")
)

private fun AssetUpdateObject.mapToDeposit() = Deposit(
    id,
    sum,
    currency,
    userId,
    LocalDateTime.parse(depositFields?.startDate),
    LocalDateTime.parse(depositFields?.endDate),
    depositFields?.interestRate ?: throw IllegalArgumentException("Deposit fields should be filled")
)

private fun Deposit.mapToResponse() = AssetResponseObject(
    sum = sum,
    currency = currency,
    userId = userId,
    type = AssetResponseObject.Type.DEPOSIT,
    depositFields = mapDepositFields(),
    id = id,
)

private fun Deposit.mapDepositFields() = DepositFields(
    startDate.toString(),
    endDate.toString(),
    interestRate,
)

private fun AssetCreateObject.mapToCash() = Cash(
    UUID.randomUUID().toString(),
    sum,
    currency,
    userId,
)

private fun AssetUpdateObject.mapToCash() = Cash(
    id,
    sum,
    currency,
    userId,
)

private fun Cash.mapToResponse() = AssetResponseObject(
    sum = sum,
    currency = currency,
    userId = userId,
    type = AssetResponseObject.Type.CASH,
    id = id,
)