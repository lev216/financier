package ru.otus.otuskotlin.financier.asset.common.helper

import ru.otus.otuskotlin.financier.asset.common.model.AssetError

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