package ru.otus.otuskotlin.financier.asset.common.persistence

import ru.otus.otuskotlin.financier.asset.common.model.Asset

data class DbAssetRequest(
    val asset: Asset,
)