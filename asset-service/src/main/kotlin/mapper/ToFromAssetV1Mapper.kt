package ru.otus.otuskotlin.financier.asset.mapper

import ru.otus.otuskotlin.financier.asset.api.v1.models.CreateUpdateAssetRequest
import ru.otus.otuskotlin.financier.asset.api.v1.models.CreateUpdateAssetRequest.Type.CASH
import ru.otus.otuskotlin.financier.asset.api.v1.models.CreateUpdateAssetRequest.Type.DEPOSIT
import ru.otus.otuskotlin.financier.asset.api.v1.models.DepositFields
import ru.otus.otuskotlin.financier.asset.api.v1.models.FoundAsset
import ru.otus.otuskotlin.financier.asset.model.Asset
import ru.otus.otuskotlin.financier.asset.model.Cash
import ru.otus.otuskotlin.financier.asset.model.Deposit
import java.time.LocalDateTime
import java.util.UUID

fun mapToAsset(request: CreateUpdateAssetRequest, id: String?): Asset = when(request.type) {
    DEPOSIT -> request.mapToDeposit(id)
    CASH -> request.mapToCash(id)
}

fun mapFromAsset(asset: Asset): FoundAsset = when(asset) {
    is Deposit -> asset.mapToResponse()
    is Cash -> asset.mapToResponse()
    else -> throw IllegalArgumentException("Asset is unknown")
}

private fun CreateUpdateAssetRequest.mapToDeposit(id: String?) = Deposit(
    id ?: UUID.randomUUID().toString(),
    sum,
    currency,
    userId,
    LocalDateTime.parse(depositFields?.startDate),
    LocalDateTime.parse(depositFields?.endDate),
    depositFields?.interestRate ?: throw IllegalArgumentException("Deposit fields should be filled")
)

private fun Deposit.mapToResponse() = FoundAsset(
    id,
    sum,
    currency,
    userId,
    FoundAsset.Type.DEPOSIT,
    mapDepositFields(),
)

private fun Deposit.mapDepositFields() = DepositFields(
    startDate.toString(),
    endDate.toString(),
    interestRate,
)

private fun CreateUpdateAssetRequest.mapToCash(id: String?) = Cash(
    id ?: UUID.randomUUID().toString(),
    sum,
    currency,
    userId,
)

private fun Cash.mapToResponse() = FoundAsset(
    id,
    sum,
    currency,
    userId,
    FoundAsset.Type.CASH,
)