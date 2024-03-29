package ru.otus.otuskotlin.financier.asset.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.financier.asset.common.model.*

data class AssetContext(
    var command: AssetCommand = AssetCommand.NONE,
    var state: AssetState = AssetState.NONE,
    val errors: MutableList<AssetError> = mutableListOf(),

    var workMode: AssetWorkMode = AssetWorkMode.PROD,
    var stubCase: AssetStub = AssetStub.NONE,

    var requestId: AssetRequestId = AssetRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var assetRequest: Asset = ASSET_NONE,
    var assetIdRequest: AssetId = AssetId.NONE,
    var assetFilterRequest: AssetFilter = AssetFilter(),

    var assetValidating: Asset = ASSET_NONE,
    var assetIdValidating: AssetId = AssetId.NONE,
    var assetFilterValidating: AssetFilter = AssetFilter(),

    var assetValidated: Asset = ASSET_NONE,
    var assetIdValidated: AssetId = AssetId.NONE,
    var assetFilterValidated: AssetFilter = AssetFilter(),

    var assetResponse: Asset = ASSET_NONE,
    val assetsResponse: MutableList<Asset> = mutableListOf(),
)