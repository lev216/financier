package ru.otus.otuskotlin.financier.asset.common.helper

import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.AssetError
import ru.otus.otuskotlin.financier.asset.common.model.AssetState

fun Throwable.asAssetError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = AssetError(
    code = code,
    group = group,
    message = message,
    field = "",
    exception = this,
)

fun AssetContext.addError(vararg error: AssetError) = errors.addAll(error)

fun AssetContext.fail(error: AssetError) {
    addError(error)
    state = AssetState.FAILING
}

fun errorValidation(
    field: String,
    violationCode: String,
    description: String,
) = AssetError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
)