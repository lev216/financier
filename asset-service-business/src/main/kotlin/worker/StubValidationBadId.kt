package ru.otus.otuskotlin.financier.asset.business.worker

import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.*
import ru.otus.otuskotlin.financier.cor.ICorChainDsl
import ru.otus.otuskotlin.financier.cor.worker

fun ICorChainDsl<AssetContext>.stubValidationBadId(title: String) = worker {
    this.title = title
    on { stubCase == AssetStub.BAD_ID && state == AssetState.RUNNING }
    handle {
        state = AssetState.FAILING
        this.errors.add(
            AssetError(
                group = "validation",
                code = "empty",
                field = "id",
                message = "value can't be empty",
            )
        )
    }
}