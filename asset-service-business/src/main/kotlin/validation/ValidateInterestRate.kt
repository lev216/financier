package ru.otus.otuskotlin.financier.asset.business.validation

import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.helper.errorValidation
import ru.otus.otuskotlin.financier.asset.common.helper.fail
import ru.otus.otuskotlin.financier.asset.common.model.Deposit
import ru.otus.otuskotlin.financier.cor.ICorChainDsl
import ru.otus.otuskotlin.financier.cor.worker
import java.math.BigDecimal

fun ICorChainDsl<AssetContext>.validateInterestRateIsPositive(title: String) = worker {
    this.title = title
    on { assetValidating is Deposit && (assetValidating as Deposit).interestRate < BigDecimal.ZERO }
    handle {
        fail(
            errorValidation(
                field = "interestRate",
                violationCode = "negative",
                description = "value should be positive or zero",
            )
        )
    }
}