package ru.otus.otuskotlin.financier.asset.business.persistence

import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.AssetState
import ru.otus.otuskotlin.financier.asset.common.persistence.DbAssetIdRequest
import ru.otus.otuskotlin.financier.cor.ICorChainDsl
import ru.otus.otuskotlin.financier.cor.worker

fun ICorChainDsl<AssetContext>.persistenceRead(title: String) = worker {
    this.title = title
    description = "Reading asset from db"
    on { state == AssetState.RUNNING }
    handle {
        val request = DbAssetIdRequest(assetIdValidated)
        val result = assetRepository.readAsset(request)
        val data = result.data
        if (result.isSuccess && data != null) {
            assetPersistentRead = data
        } else {
            state = AssetState.FAILING
            errors.addAll(result.errors)
        }
    }
}