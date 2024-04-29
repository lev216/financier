package ru.otus.otuskotlin.financier.asset.common.persistence

interface IAssetRepository {
    suspend fun createAsset(request: DbAssetRequest): DbAssetResponse
    suspend fun readAsset(request: DbAssetIdRequest): DbAssetResponse
    suspend fun updateAsset(request: DbAssetRequest): DbAssetResponse
    suspend fun deleteAsset(request: DbAssetIdRequest): DbAssetResponse
    suspend fun searchAsset(request: DbAssetFilterRequest): DbAssetsResponse

    companion object {
        val NONE = object : IAssetRepository {
            override suspend fun createAsset(request: DbAssetRequest): DbAssetResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readAsset(request: DbAssetIdRequest): DbAssetResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updateAsset(request: DbAssetRequest): DbAssetResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deleteAsset(request: DbAssetIdRequest): DbAssetResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchAsset(request: DbAssetFilterRequest): DbAssetsResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}