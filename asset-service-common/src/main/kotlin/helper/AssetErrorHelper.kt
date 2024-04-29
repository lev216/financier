package ru.otus.otuskotlin.financier.asset.common.helper

import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.exception.RepositoryConcurrencyException
import ru.otus.otuskotlin.financier.asset.common.model.AssetError
import ru.otus.otuskotlin.financier.asset.common.model.AssetLock
import ru.otus.otuskotlin.financier.asset.common.model.AssetState
import java.lang.Exception

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

fun errorAdministration(
    field: String = "",
    violationCode: String,
    description: String,
    exception: Exception? = null,
) = AssetError(
    field = field,
    code = "administration-$violationCode",
    group = "administration",
    message = "Microservice management error: $description",
    exception = exception,
)

fun errorRepositoryConcurrency(
    expectedLock: AssetLock,
    actualLock: AssetLock?,
    exception: Exception? = null,
) = AssetError(
    field = "lock",
    code = "concurrency",
    group = "repo",
    message = "The object has been changed concurrently by another user or process",
    exception = exception ?: RepositoryConcurrencyException(expectedLock, actualLock)
)

val assetErrorNotFound = AssetError(
    field = "id",
    message = "Not found",
    code = "not-found",
)

val assetErrorEmptyId = AssetError(
    field = "id",
    message = "Id can't be empty or null",
    code = "id-empty"
)