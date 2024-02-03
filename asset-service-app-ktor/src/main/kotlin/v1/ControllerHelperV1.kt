package ru.otus.otuskotlin.financier.asset.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.financier.asset.api.v1.models.IRequest
import ru.otus.otuskotlin.financier.asset.api.v1.models.IResponse
import ru.otus.otuskotlin.financier.asset.app.common.controllerHelper
import ru.otus.otuskotlin.financier.asset.business.processor.AssetProcessor
import ru.otus.otuskotlin.financier.asset.api.v1.factory.AssetContextFactory
import ru.otus.otuskotlin.financier.asset.api.v1.mapper.toResponse

suspend inline fun <reified Q : IRequest, reified R : IResponse> ApplicationCall.processV1(
    assetProcessor: AssetProcessor
) = assetProcessor.controllerHelper(
    { AssetContextFactory.createContext(receive<Q>()) },
    { respond(toResponse()) }
)