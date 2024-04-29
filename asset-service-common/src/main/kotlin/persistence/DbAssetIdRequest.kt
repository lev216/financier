package ru.otus.otuskotlin.financier.asset.common.persistence

import ru.otus.otuskotlin.financier.asset.common.model.Asset
import ru.otus.otuskotlin.financier.asset.common.model.AssetId
import ru.otus.otuskotlin.financier.asset.common.model.AssetLock

data class DbAssetIdRequest(
    val id: AssetId,
    val lock: AssetLock = AssetLock.NONE,
) {
    constructor(asset: Asset) : this(asset.id, asset.lock)
}
