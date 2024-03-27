package ru.otus.otuskotlin.financier.asset.business.group

import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.AssetState
import ru.otus.otuskotlin.financier.asset.common.model.AssetWorkMode
import ru.otus.otuskotlin.financier.cor.ICorChainDsl
import ru.otus.otuskotlin.financier.cor.chain

fun ICorChainDsl<AssetContext>.stubs(title: String, block: ICorChainDsl<AssetContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == AssetWorkMode.STUB && state == AssetState.RUNNING }
}