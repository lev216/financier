package ru.otus.otuskotlin.financier.asset.app.common

import ru.otus.otuskotlin.financier.asset.business.processor.AssetProcessor
import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.helper.asAssetError
import ru.otus.otuskotlin.financier.asset.common.model.AssetState.FAILING
import ru.otus.otuskotlin.financier.asset.common.model.AssetState.RUNNING

suspend inline fun <T> AssetProcessor.controllerHelper(
    getContext: () -> AssetContext,
    toResponse: AssetContext.() -> T,
): T {
    val context = getContext()
    return try {
        exec(context)
        if (context.state == RUNNING) context.assetResponse = context.assetValidated
        context.toResponse()
    } catch (e: Throwable) {
        context.state = FAILING
        context.errors.add(e.asAssetError())
        exec(context)
        context.toResponse()
    }
}