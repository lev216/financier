package ru.otus.otuskotlin.financier.asset.business.validation

import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.AssetState
import ru.otus.otuskotlin.financier.cor.ICorChainDsl
import ru.otus.otuskotlin.financier.cor.chain

fun ICorChainDsl<AssetContext>.validation(block: ICorChainDsl<AssetContext>.() -> Unit) = chain {
    block()
    title = "Validation"
    on { state == AssetState.RUNNING}
}