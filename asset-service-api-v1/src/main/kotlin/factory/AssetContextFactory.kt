package ru.otus.otuskotlin.financier.asset.api.v1.factory

import kotlinx.datetime.Clock
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.financier.asset.api.v1.mapper.mapToAsset
import ru.otus.otuskotlin.financier.asset.api.v1.models.*
import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object AssetContextFactory {

    private val logger: Logger = LoggerFactory.getLogger(AssetContextFactory::class.java)
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    fun createContext(request: IRequest) = AssetContext(
        command = request.toAssetCommand(),
        workMode = request.requestDebug?.debug.toAssetWorkMode(),
        state = AssetState.RUNNING,
        stubCase = request.requestDebug?.debug.toAssetStub(),
        requestId = AssetRequestId(request.requestId),
        timeStart = Clock.System.now(),
        assetRequest = mapToAsset(request),
        assetIdRequest = request.toAssetId(),
        assetFilterRequest = request.toAssetSearchFilter()
    ).also {
        logger.info("Create context for request: $request")
    }

    private fun IRequest.toAssetCommand() = when(this) {
        is AssetCreateRequest -> AssetCommand.CREATE
        is AssetUpdateRequest -> AssetCommand.UPDATE
        is AssetDeleteRequest -> AssetCommand.DELETE
        is AssetReadRequest -> AssetCommand.READ
        is AssetSearchRequest -> AssetCommand.SEARCH
        else -> AssetCommand.NONE
    }

    private fun IRequest.toAssetSearchFilter() = if (this is AssetSearchRequest) {
        this.assetFilter.toAssetFilter()
    } else {
        AssetFilter()
    }

    private fun AssetSearchFilter.toAssetFilter() = AssetFilter(
        id = this.id.toAssetId(),
        userId = this.userId.toUserId(),
        startDate = this.startDate.toLocalDate() ?: LocalDate.MIN,
        endDate = this.endDate.toLocalDate() ?: LocalDate.MAX,
        type = this.type.toAssetSearchType()
    )

    private fun String?.toAssetId() = this?.let { AssetId(this) } ?: AssetId.NONE

    private fun String?.toUserId() = this?.let { UserId(this) } ?: UserId.NONE

    private fun String?.toLocalDate() = this?.let { LocalDate.parse(this, dateTimeFormatter) }

    private fun AssetType?.toAssetSearchType() = when(this) {
        AssetType.CASH -> AssetSearchType.CASH
        AssetType.DEPOSIT -> AssetSearchType.DEPOSIT
        else -> AssetSearchType.NONE
    }

    private fun AssetDebug?.toAssetWorkMode() = when(this?.mode) {
        AssetRequestDebugMode.TEST -> AssetWorkMode.TEST
        AssetRequestDebugMode.STUB -> AssetWorkMode.STUB
        AssetRequestDebugMode.PROD -> AssetWorkMode.PROD
        else -> AssetWorkMode.PROD
    }

    private fun AssetDebug?.toAssetStub() = when(this?.stub) {
        AssetRequestDebugStubs.SUCCESS -> AssetStub.SUCCESS
        AssetRequestDebugStubs.NOT_FOUND -> AssetStub.NOT_FOUND
        AssetRequestDebugStubs.BAD_ID -> AssetStub.BAD_ID
        AssetRequestDebugStubs.NEGATIVE_SUM -> AssetStub.NEGATIVE_SUM
        AssetRequestDebugStubs.CANNOT_DELETE -> AssetStub.CANNOT_DELETE
        AssetRequestDebugStubs.BAD_TYPE -> AssetStub.BAD_TYPE
        else -> AssetStub.NONE
    }

    private fun IRequest.toAssetId(): AssetId = when(this) {
        is AssetReadRequest -> AssetId(this.asset.id)
        is AssetDeleteRequest -> AssetId(this.asset.id)
        else -> AssetId.NONE
    }

}