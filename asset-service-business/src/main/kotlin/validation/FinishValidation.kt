package ru.otus.otuskotlin.financier.asset.business.validation

import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.AssetState
import ru.otus.otuskotlin.financier.cor.ICorChainDsl
import ru.otus.otuskotlin.financier.cor.worker

fun ICorChainDsl<AssetContext>.finishAssetValidation(title: String) = worker {
    this.title = title
    on { state == AssetState.RUNNING }
    handle {
        assetValidated = assetValidating
    }
}

fun ICorChainDsl<AssetContext>.finishAssetIdValidation(title: String) = worker {
    this.title = title
    on { state == AssetState.RUNNING }
    handle {
        assetIdValidated = assetIdValidating
    }
}

fun ICorChainDsl<AssetContext>.finishAssetFilterValidation(title: String) = worker {
    this.title = title
    on { state == AssetState.RUNNING }
    handle {
        assetFilterValidated = assetFilterValidating
    }
}