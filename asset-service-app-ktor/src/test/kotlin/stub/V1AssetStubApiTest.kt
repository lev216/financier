package ru.otus.otuskotlin.financier.asset.stub

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import ru.otus.otuskotlin.financier.asset.api.v1.models.*
import ru.otus.otuskotlin.financier.asset.module
import java.math.BigDecimal

class V1AssetStubApiTest {

    @Test
    fun create() = v1TestApplication { client ->
        val response = client.post("/v1/asset/create") {
            val request = AssetCreateRequest(
                "create",
                requestId = "requestId",
                asset = AssetCreateObject(
                    sum = BigDecimal.TEN,
                    currency = "USD",
                    userId = "userId",
                    type = AssetType.CASH,
                ),
                requestDebug = AssetRequestDebug(
                    debug = AssetDebug(
                        mode = AssetRequestDebugMode.STUB,
                        stub = AssetRequestDebugStubs.SUCCESS,
                    )
                ),
            )
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        val assetResponse = response.body<AssetCreateResponse>()
        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        assertThat(assetResponse.asset?.id).isEqualTo("da5db9d8-b13d-4094-959e-2fc57482ae70")
    }

    private fun v1TestApplication(function: suspend (HttpClient) -> Unit): Unit = testApplication {
        application { module() }
        val client = createClient {
            install(ContentNegotiation) {
                jackson {
                    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                    enable(SerializationFeature.INDENT_OUTPUT)
                    writerWithDefaultPrettyPrinter()
                }
            }
        }
        function(client)
    }
}