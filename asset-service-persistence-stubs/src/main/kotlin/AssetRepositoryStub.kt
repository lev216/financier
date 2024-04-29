package ru.otus.otuskotlin.financier.asset.persistence.inmemory.stubs

import ru.otus.otuskotlin.financier.asset.common.persistence.*
import ru.otus.otuskotlin.financier.asset.stubs.AssetStubs

class AssetRepositoryStub : IAssetRepository {

    var assetTypeForTest: AsserType = AsserType.CASH

    override suspend fun createAsset(request: DbAssetRequest): DbAssetResponse {
        return DbAssetResponse(
            data = getAsset(),
            isSuccess = true,
        )
    }

    override suspend fun readAsset(request: DbAssetIdRequest): DbAssetResponse {
        return DbAssetResponse(
            data = getAsset(),
            isSuccess = true,
        )
    }

    override suspend fun updateAsset(request: DbAssetRequest): DbAssetResponse {
        return DbAssetResponse(
            data = getAsset(),
            isSuccess = true,
        )
    }

    override suspend fun deleteAsset(request: DbAssetIdRequest): DbAssetResponse {
        return DbAssetResponse(
            data = getAsset(),
            isSuccess = true,
        )
    }

    override suspend fun searchAsset(request: DbAssetFilterRequest): DbAssetsResponse {
        return DbAssetsResponse(
            data = AssetStubs.prepareFullSearchList(),
            isSuccess = true,
        )
    }

    private fun getAsset() = when (assetTypeForTest) {
        AsserType.CASH -> AssetStubs.getCash()
        AsserType.DEPOSIT -> AssetStubs.getDeposit()
    }

    enum class AsserType {
        CASH, DEPOSIT
    }
}