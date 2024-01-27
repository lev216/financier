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
    var assetFilter: AssetFilter = AssetFilter(),
    var assetResponse: Asset = ASSET_NONE,
    val assets: MutableList<Asset> = mutableListOf(),
)