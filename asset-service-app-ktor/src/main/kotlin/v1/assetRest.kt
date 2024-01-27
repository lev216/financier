package ru.otus.otuskotlin.financier.asset.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.financier.asset.business.processor.AssetProcessor

fun Route.v1Asset(processor: AssetProcessor) {
    route("/asset") {
        post("/create") {
            call.createAsset(processor)
        }
        post("/read") {
            call.readAsset(processor)
        }
        post("/update") {
            call.updateAsset(processor)
        }
        post("/delete") {
            call.deleteAsset(processor)
        }
        post("/search") {
            call.searchAssets(processor)
        }
    }
}