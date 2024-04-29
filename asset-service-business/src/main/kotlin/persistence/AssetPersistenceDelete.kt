package ru.otus.otuskotlin.financier.asset.business.persistence

import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.AssetState
import ru.otus.otuskotlin.financier.asset.common.persistence.DbAssetIdRequest
import ru.otus.otuskotlin.financier.cor.ICorChainDsl
import ru.otus.otuskotlin.financier.cor.worker

fun ICorChainDsl<AssetContext>.persistenceDelete(title: String) = worker {
    this.title = title
    description = "Remove asset from db by id"
    on { state == AssetState.RUNNING }
    handle {
        val request = DbAssetIdRequest(assetPersistentPrepared)
        val result = assetRepository.deleteAsset(request)
        if (!result.isSuccess) {
            state = AssetState.FAILING
            errors.addAll(result.errors)
        }
        assetPersistentDone = assetPersistentRead
    }
}