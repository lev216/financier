package ru.otus.otuskotlin.financier.asset.business.worker

import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.AssetState
import ru.otus.otuskotlin.financier.asset.common.model.AssetStub
import ru.otus.otuskotlin.financier.asset.common.model.Cash
import ru.otus.otuskotlin.financier.asset.common.model.Deposit
import ru.otus.otuskotlin.financier.asset.stubs.AssetStubs
import ru.otus.otuskotlin.financier.cor.ICorChainDsl
import ru.otus.otuskotlin.financier.cor.worker

fun ICorChainDsl<AssetContext>.stubCreateCashSuccess(title: String) = worker {
    this.title = title
    on { assetRequest is Cash && stubCase == AssetStub.SUCCESS && state == AssetState.RUNNING }
    handle {
        state = AssetState.FINISHING
        assetResponse = AssetStubs.getCash()
    }
}

fun ICorChainDsl<AssetContext>.stubCreateDepositSuccess(title: String) = worker {
    this.title = title
    on { assetRequest is Deposit && stubCase == AssetStub.SUCCESS && state == AssetState.RUNNING }
    handle {
        state = AssetState.FINISHING
        assetResponse = AssetStubs.getDeposit()
    }
}