package ru.otus.otuskotlin.financier.asset.business.worker

import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.AssetState
import ru.otus.otuskotlin.financier.asset.common.model.AssetStub
import ru.otus.otuskotlin.financier.asset.stubs.AssetStubs
import ru.otus.otuskotlin.financier.cor.ICorChainDsl
import ru.otus.otuskotlin.financier.cor.worker

fun ICorChainDsl<AssetContext>.stubSearchSuccess(title: String) = worker {
    this.title = title
    on { stubCase == AssetStub.SUCCESS && state == AssetState.RUNNING }
    handle {
        state = AssetState.FINISHING
        assetsResponse.addAll(AssetStubs.prepareFullSearchList())
    }
}