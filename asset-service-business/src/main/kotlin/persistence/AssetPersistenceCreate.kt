package ru.otus.otuskotlin.financier.asset.business.persistence

import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.AssetState
import ru.otus.otuskotlin.financier.asset.common.persistence.DbAssetRequest
import ru.otus.otuskotlin.financier.cor.ICorChainDsl
import ru.otus.otuskotlin.financier.cor.worker

fun ICorChainDsl<AssetContext>.persistenceCreate(title: String) = worker {
    this.title = title
    description = "Add asset to db"
    on { state == AssetState.RUNNING }
    handle {
        val request = DbAssetRequest(assetPersistentPrepared)
        val result = assetRepository.createAsset(request)
        val resultAsset = result.data
        if (result.isSuccess && resultAsset != null) {
            assetPersistentDone = resultAsset
        } else {
            state = AssetState.FAILING
            errors.addAll(result.errors)
        }
    }
}