package ru.otus.otuskotlin.financier.asset.business.persistence

import ru.otus.otuskotlin.financier.asset.business.exception.ServiceException
import ru.otus.otuskotlin.financier.asset.business.utils.copy
import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.Asset
import ru.otus.otuskotlin.financier.asset.common.model.AssetState
import ru.otus.otuskotlin.financier.asset.common.model.Cash
import ru.otus.otuskotlin.financier.asset.common.model.Deposit
import ru.otus.otuskotlin.financier.cor.ICorChainDsl
import ru.otus.otuskotlin.financier.cor.worker

fun ICorChainDsl<AssetContext>.prepareUpdate(title: String) = worker {
    this.title = title
    description = "Prepare data to be updated in db"
    on { state == AssetState.RUNNING }
    handle {
        assetPersistentPrepared = assetPersistentRead.copy().also { it.mapValidated(assetValidated) }
    }
}

private fun Asset.mapValidated(validated: Asset) = if (this is Deposit && validated is Deposit) {
    this.also {
        it.sum = validated.sum
        it.startDate = validated.startDate
        it.endDate = validated.endDate
        it.interestRate = validated.interestRate
    }
} else if (this is Cash && validated is Cash) {
    this.also {
        it.sum = validated.sum
    }
} else throw ServiceException("Can't map ${this::class.java} and ${validated::class.java}")