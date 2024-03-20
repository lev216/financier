package ru.otus.otuskotlin.financier.asset.business.worker

import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.AssetError
import ru.otus.otuskotlin.financier.asset.common.model.AssetState
import ru.otus.otuskotlin.financier.asset.common.model.AssetStub
import ru.otus.otuskotlin.financier.cor.ICorChainDsl
import ru.otus.otuskotlin.financier.cor.worker

fun ICorChainDsl<AssetContext>.stubDbError(title: String) = worker {
    this.title = title
    on { stubCase == AssetStub.DB_ERROR && state == AssetState.RUNNING }
    handle {
        this.errors.add(
            AssetError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}