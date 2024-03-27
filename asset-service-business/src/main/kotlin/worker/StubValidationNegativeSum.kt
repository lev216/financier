package ru.otus.otuskotlin.financier.asset.business.worker

import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.AssetError
import ru.otus.otuskotlin.financier.asset.common.model.AssetState
import ru.otus.otuskotlin.financier.asset.common.model.AssetStub
import ru.otus.otuskotlin.financier.cor.ICorChainDsl
import ru.otus.otuskotlin.financier.cor.worker

fun ICorChainDsl<AssetContext>.stubValidationNegativeSum(title: String) = worker {
    this.title = title
    on { stubCase == AssetStub.NEGATIVE_SUM && state == AssetState.RUNNING }
    handle {
        state = AssetState.FAILING
        this.errors.add(
            AssetError(
                group = "validation",
                code = "negative",
                field = "sum",
                message = "value should be positive or zero",
            )
        )
    }
}