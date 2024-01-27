package ru.otus.otuskotlin.financier.asset.mapper

import ru.otus.otuskotlin.financier.asset.api.v1.mapper.mapFromAsset
import ru.otus.otuskotlin.financier.asset.api.v1.models.*
import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.AssetCommand
import ru.otus.otuskotlin.financier.asset.common.model.AssetError
import ru.otus.otuskotlin.financier.asset.common.model.AssetState
import ru.otus.otuskotlin.financier.asset.exception.AssetCommandUnknownException

fun AssetContext.toResponse() = when(command) {
    AssetCommand.CREATE -> toAssetCreateResponse()
    AssetCommand.READ -> toAssetReadResponse()
    AssetCommand.UPDATE -> toAssetUpdateResponse()
    AssetCommand.DELETE -> toAssetDeleteResponse()
    AssetCommand.SEARCH -> toAssetSearchResponse()
    else -> throw AssetCommandUnknownException(this.command)
}

private fun AssetContext.toAssetCreateResponse() = AssetCreateResponse(
    requestId = this.requestId.asString(),
    result = if (state == AssetState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toErrors(),
    asset = mapFromAsset(this.assetResponse),
)

private fun AssetContext.toAssetReadResponse() = AssetReadResponse(
    requestId = this.requestId.asString(),
    result = if (state == AssetState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toErrors(),
    asset = mapFromAsset(this.assetResponse),
)

private fun AssetContext.toAssetUpdateResponse() = AssetUpdateResponse(
    requestId = this.requestId.asString(),
    result = if (state == AssetState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toErrors(),
    asset = mapFromAsset(this.assetResponse),
)

private fun AssetContext.toAssetDeleteResponse() = AssetDeleteResponse(
    requestId = this.requestId.asString(),
    result = if (state == AssetState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toErrors(),
    asset = mapFromAsset(this.assetResponse),
)

private fun AssetContext.toAssetSearchResponse() = AssetSearchResponse(
    requestId = this.requestId.asString(),
    result = if (state == AssetState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toErrors(),
    assets = assets.map { mapFromAsset(it) }.toList().takeIf { it.isNotEmpty() }
)

private fun List<AssetError>.toErrors(): List<Error>? = this
    .map { it.toError() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun AssetError.toError() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)