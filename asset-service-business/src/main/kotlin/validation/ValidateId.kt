package ru.otus.otuskotlin.financier.asset.business.validation

import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.helper.errorValidation
import ru.otus.otuskotlin.financier.asset.common.helper.fail
import ru.otus.otuskotlin.financier.asset.common.model.AssetId
import ru.otus.otuskotlin.financier.asset.common.model.UserId
import ru.otus.otuskotlin.financier.cor.ICorChainDsl
import ru.otus.otuskotlin.financier.cor.worker

fun ICorChainDsl<AssetContext>.validateAssetIdIsNotEmpty(title: String) = worker {
    this.title = title
    on { assetIdValidating.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "id",
                violationCode = "empty",
                description = "value can't be empty",
            )
        )
    }
}

fun ICorChainDsl<AssetContext>.validateAssetIdProperFormat(title: String) = worker {
    this.title = title
    on { assetIdValidating!= AssetId.NONE && !assetIdValidating.asString().matches(AssetId.REGEX) }
    handle {
        val encodedId = assetValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "id",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}

fun ICorChainDsl<AssetContext>.validateUserIdIsNotEmpty(title: String) = worker {
    this.title = title
    on { assetValidating.userId.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "userId",
                violationCode = "empty",
                description = "value can't be empty",
            )
        )
    }
}

fun ICorChainDsl<AssetContext>.validateUserIdProperFormat(title: String) = worker {
    this.title = title
    on { assetValidating.userId != UserId.NONE && !assetValidating.userId.asString().matches(UserId.REGEX) }
    handle {
        val encodedId = assetValidating.userId.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "userId",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}