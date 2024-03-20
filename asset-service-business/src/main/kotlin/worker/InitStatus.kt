package ru.otus.otuskotlin.financier.asset.business.worker

import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.AssetState
import ru.otus.otuskotlin.financier.cor.ICorChainDsl
import ru.otus.otuskotlin.financier.cor.worker

fun ICorChainDsl<AssetContext>.initStatus(title: String) = worker {
    this.title = title
    on { state == AssetState.NONE }
    handle { state = AssetState.RUNNING }
}