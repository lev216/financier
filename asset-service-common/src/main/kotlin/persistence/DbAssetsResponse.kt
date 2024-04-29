package ru.otus.otuskotlin.financier.asset.common.persistence

import ru.otus.otuskotlin.financier.asset.common.model.Asset
import ru.otus.otuskotlin.financier.asset.common.model.AssetError

data class DbAssetsResponse(
    override val data: List<Asset>?,
    override val isSuccess: Boolean,
    override val errors: List<AssetError> = emptyList(),
) : IDbResponse<List<Asset>> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbAssetsResponse(emptyList(), true)
        fun success(result: List<Asset>) = DbAssetsResponse(result, true)
        fun error(errors: List<AssetError>) = DbAssetsResponse(null, false, errors)
        fun error(error: AssetError) = DbAssetsResponse(null, false, listOf(error))
    }
}
