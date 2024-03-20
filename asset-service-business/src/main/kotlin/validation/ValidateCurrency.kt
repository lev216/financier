package ru.otus.otuskotlin.financier.asset.business.validation

import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.helper.errorValidation
import ru.otus.otuskotlin.financier.asset.common.helper.fail
import ru.otus.otuskotlin.financier.cor.ICorChainDsl
import ru.otus.otuskotlin.financier.cor.worker

fun ICorChainDsl<AssetContext>.validateCurrencyIsNotEmpty(title: String) = worker {
    this.title = title
    on { assetValidating.currency.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "currency",
                violationCode = "empty",
                description = "value can't be empty",
            )
        )
    }
}