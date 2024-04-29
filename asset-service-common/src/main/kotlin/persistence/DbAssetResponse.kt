package ru.otus.otuskotlin.financier.asset.common.persistence

import ru.otus.otuskotlin.financier.asset.common.helper.assetErrorEmptyId
import ru.otus.otuskotlin.financier.asset.common.helper.assetErrorNotFound
import ru.otus.otuskotlin.financier.asset.common.helper.errorRepositoryConcurrency
import ru.otus.otuskotlin.financier.asset.common.model.Asset
import ru.otus.otuskotlin.financier.asset.common.model.AssetError
import ru.otus.otuskotlin.financier.asset.common.model.AssetLock

data class DbAssetResponse(
    override val data: Asset?,
    override val isSuccess: Boolean,
    override val errors: List<AssetError> = emptyList(),
) : IDbResponse<Asset> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbAssetResponse(null, true)
        fun success(result: Asset) = DbAssetResponse(result, true)
        fun error(errors: List<AssetError>, data: Asset? = null) = DbAssetResponse(data, false, errors)
        fun error(error: AssetError, data: Asset? = null) = DbAssetResponse(data, false, listOf(error))

        val errorEmptyId = error(assetErrorEmptyId)

        fun errorConcurrent(lock: AssetLock, asset: Asset?) = error(
            errorRepositoryConcurrency(lock, asset?.lock?.let { AssetLock(it.asString()) }),
            asset
        )

        val errorNotFound = error(assetErrorNotFound)
    }
}
