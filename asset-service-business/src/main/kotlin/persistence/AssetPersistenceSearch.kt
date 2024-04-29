package ru.otus.otuskotlin.financier.asset.business.persistence

import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.AssetState
import ru.otus.otuskotlin.financier.asset.common.persistence.DbAssetFilterRequest
import ru.otus.otuskotlin.financier.cor.ICorChainDsl
import ru.otus.otuskotlin.financier.cor.worker

fun ICorChainDsl<AssetContext>.persistenceSearch(title: String) = worker {
    this.title = title
    description = "Asset search in db by filtering"
    on { state == AssetState.RUNNING }
    handle {
        val request = DbAssetFilterRequest(
            startDate = assetFilterValidated.startDate,
            endDate = assetFilterValidated.endDate,
            userId = assetFilterValidated.userId,
            type = assetFilterValidated.type,
        )
        val result = assetRepository.searchAsset(request)
        val resultAssets = result.data
        if (result.isSuccess && resultAssets != null) {
            assetsPersistentDone = resultAssets.toMutableList()
        } else {
            state = AssetState.FAILING
            errors.addAll(result.errors)
        }
    }
}