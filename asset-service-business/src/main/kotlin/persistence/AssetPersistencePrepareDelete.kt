package ru.otus.otuskotlin.financier.asset.business.persistence

import ru.otus.otuskotlin.financier.asset.business.utils.copy
import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.AssetState
import ru.otus.otuskotlin.financier.cor.ICorChainDsl
import ru.otus.otuskotlin.financier.cor.worker

fun ICorChainDsl<AssetContext>.prepareDelete(title: String) = worker {
    this.title = title
    description = "Prepare data to be removed from db"
    on { state == AssetState.RUNNING }
    handle {
        assetPersistentPrepared = assetPersistentRead.copy()
    }
}