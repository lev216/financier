package ru.otus.otuskotlin.financier.asset.business.general

import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.AssetState
import ru.otus.otuskotlin.financier.asset.common.model.AssetWorkMode
import ru.otus.otuskotlin.financier.cor.ICorChainDsl
import ru.otus.otuskotlin.financier.cor.worker

fun ICorChainDsl<AssetContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Data preparation to respond on user request"
    on { workMode != AssetWorkMode.STUB }
    handle {
        assetResponse = assetPersistentDone
        assetsResponse = assetsPersistentDone
        state = when(val st = state) {
            AssetState.RUNNING -> AssetState.FINISHING
            else -> st
        }
    }
}