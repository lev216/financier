package ru.otus.otuskotlin.financier.asset.common.model

data class AssetError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
)
