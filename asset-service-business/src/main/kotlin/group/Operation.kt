package ru.otus.otuskotlin.financier.asset.business.group

import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.AssetCommand
import ru.otus.otuskotlin.financier.asset.common.model.AssetState
import ru.otus.otuskotlin.financier.cor.ICorChainDsl
import ru.otus.otuskotlin.financier.cor.chain

fun ICorChainDsl<AssetContext>.operation(title: String, command: AssetCommand, block: ICorChainDsl<AssetContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { this.command == command && state == AssetState.RUNNING }
}