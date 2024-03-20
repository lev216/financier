package ru.otus.otuskotlin.financier.asset.business.validation

import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.helper.errorValidation
import ru.otus.otuskotlin.financier.asset.common.helper.fail
import ru.otus.otuskotlin.financier.asset.common.model.Deposit
import ru.otus.otuskotlin.financier.cor.ICorChainDsl
import ru.otus.otuskotlin.financier.cor.worker
import java.time.LocalDate

fun ICorChainDsl<AssetContext>.validateStartDateNotInFuture(title: String) = worker {
    this.title = title
    on { assetValidating is Deposit && (assetValidating as Deposit).startDate.isAfter(LocalDate.now()) }
    handle {
        fail(
            errorValidation(
                field = "startDate",
                violationCode = "dateInFuture",
                description = "start date shouldn't be in future",
            )
        )
    }
}

fun ICorChainDsl<AssetContext>.validateEndDateIsAfterStartDate(title: String) = worker {
    this.title = title
    on { assetValidating is Deposit && (assetValidating as Deposit).startDate.isAfter((assetValidating as Deposit).endDate) }
    handle {
        fail(
            errorValidation(
                field = "endDate",
                violationCode = "dateConfusion",
                description = "end date shouldn't be before start date",
            )
        )
    }
}

fun ICorChainDsl<AssetContext>.validateEndDateIsNotInPast(title: String) = worker {
    this.title = title
    on { assetValidating is Deposit && (assetValidating as Deposit).endDate.isBefore(LocalDate.now()) }
    handle {
        fail(
            errorValidation(
                field = "endDate",
                violationCode = "endDatePassed",
                description = "end date should be in the future",
            )
        )
    }
}