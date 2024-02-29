package ru.otus.otuskotlin.financier.asset

import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import org.slf4j.event.Level
import ru.otus.otuskotlin.financier.asset.business.processor.AssetProcessor
import ru.otus.otuskotlin.financier.asset.service.CbRfCurrenciesListener
import ru.otus.otuskotlin.financier.asset.v1.v1Asset

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

private val runtime = Runtime.getRuntime()

fun Application.module(
    processor: AssetProcessor = AssetProcessor(),
) {
    install(CachingHeaders)
    install(DefaultHeaders)
    install(AutoHeadResponse)
    install(CallLogging) {
        level = Level.INFO
    }
    install(CORS) {
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }
    install(WebSockets) {

    }

    routing {
        install(ContentNegotiation) {
            jackson {}
        }
        route("/v1") {
            get("/health") {
                call.respond(HttpStatusCode.OK)
            }
            v1Asset(processor)
            webSocket("/cbRfCurrencies") {
                //TODO: return currencies from Redis
                send(Frame.Text("{\"currencies\":{\"AUD\":59.0059}}"))
            }
        }
    }

    val jobs = Jobs(listOf(
        CbRfCurrenciesListener(config = Config)
    ))
    jobs.start()
    runtime.addShutdownHook(Thread {
        jobs.stop()
    })
}