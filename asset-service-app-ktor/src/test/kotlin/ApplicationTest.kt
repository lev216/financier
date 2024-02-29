package ru.otus.otuskotlin.financier.asset

import io.ktor.client.request.*
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.testing.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import ru.otus.otuskotlin.financier.asset.business.processor.AssetProcessor

class ApplicationTest {

    @Test
    fun `check health`() = testApplication {
        application { module(AssetProcessor()) }
        val response = client.get("/v1/health")
        assertThat(response).isNotNull
        assertThat(response.status).isEqualTo(OK)
    }
}