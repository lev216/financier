package ru.otus.otuskotlin.financier.asset.v1

import io.ktor.server.application.*
import ru.otus.otuskotlin.financier.asset.api.v1.models.*
import ru.otus.otuskotlin.financier.asset.business.processor.AssetProcessor

suspend fun ApplicationCall.createAsset(processor: AssetProcessor) {
    processV1<AssetCreateRequest, AssetCreateResponse>(processor)
}

suspend fun ApplicationCall.readAsset(processor: AssetProcessor) {
    processV1<AssetReadRequest, AssetReadResponse>(processor)
}

suspend fun ApplicationCall.updateAsset(processor: AssetProcessor) {
    processV1<AssetUpdateRequest, AssetUpdateResponse>(processor)
}

suspend fun ApplicationCall.deleteAsset(processor: AssetProcessor) {
    processV1<AssetDeleteRequest, AssetDeleteResponse>(processor)
}

suspend fun ApplicationCall.searchAssets(processor: AssetProcessor) {
    processV1<AssetSearchRequest, AssetSearchResponse>(processor)
}
