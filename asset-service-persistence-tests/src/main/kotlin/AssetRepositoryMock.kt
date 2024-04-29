package ru.otus.otuskotlin.financier.asset.persistence.tests

import ru.otus.otuskotlin.financier.asset.common.persistence.*

class AssetRepositoryMock(
    private val invokeCreateAsset: (DbAssetRequest) -> DbAssetResponse = { DbAssetResponse.MOCK_SUCCESS_EMPTY },
    private val invokeReadAsset: (DbAssetIdRequest) -> DbAssetResponse = { DbAssetResponse.MOCK_SUCCESS_EMPTY },
    private val invokeUpdateAsset: (DbAssetRequest) -> DbAssetResponse = { DbAssetResponse.MOCK_SUCCESS_EMPTY },
    private val invokeDeleteAsset: (DbAssetIdRequest) -> DbAssetResponse = { DbAssetResponse.MOCK_SUCCESS_EMPTY },
    private val invokeSearchAssets: (DbAssetFilterRequest) -> DbAssetsResponse = { DbAssetsResponse.MOCK_SUCCESS_EMPTY },
) : IAssetRepository {

    override suspend fun createAsset(request: DbAssetRequest): DbAssetResponse = invokeCreateAsset(request)

    override suspend fun readAsset(request: DbAssetIdRequest): DbAssetResponse = invokeReadAsset(request)

    override suspend fun updateAsset(request: DbAssetRequest): DbAssetResponse = invokeUpdateAsset(request)

    override suspend fun deleteAsset(request: DbAssetIdRequest): DbAssetResponse = invokeDeleteAsset(request)

    override suspend fun searchAsset(request: DbAssetFilterRequest): DbAssetsResponse = invokeSearchAssets(request)
}