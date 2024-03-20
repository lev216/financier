package ru.otus.otuskotlin.financier.asset.stub

import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.*
import io.ktor.server.testing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.Test
import ru.otus.otuskotlin.financier.asset.module

class WebSocketStubTest {

    @Test
    fun testCbRfCurrencies() = testApplication {
        application { module() }
        val client = createClient {
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(Json)
            }
        }
        client.webSocket("/v1/cbRfCurrencies") {
            val response = receiveDeserialized<CurrenciesResponse>()
            assertThat(response).isEqualTo(CurrenciesResponse(mapOf("AUD" to 59.0059)))
        }
    }
}

@Serializable
private data class CurrenciesResponse(val currencies: Map<String, Double>? = null)