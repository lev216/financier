package ru.otus.otuskotlin.financier.asset.api.v1.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import ru.otus.otuskotlin.financier.asset.api.v1.models.*
import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.*

class AssetResponseV1MapperTest {
    private val contextCreate = AssetContext(
        command = AssetCommand.CREATE,
        requestId = AssetRequestId("id"),
        state = AssetState.FAILING,
        errors = mutableListOf(
            AssetError("code", "group", "id", "Bad id"),
        ),
    )

    private val contextRead = AssetContext(
        command = AssetCommand.READ,
        requestId = AssetRequestId("id"),
        state = AssetState.RUNNING,
    )

    private val contextUpdate = AssetContext(
        command = AssetCommand.UPDATE,
        requestId = AssetRequestId("id"),
        state = AssetState.RUNNING,
    )

    private val contextDelete = AssetContext(
        command = AssetCommand.DELETE,
        requestId = AssetRequestId("id"),
        state = AssetState.RUNNING,
    )

    private val contextSearch = AssetContext(
        command = AssetCommand.SEARCH,
        requestId = AssetRequestId("id"),
        state = AssetState.RUNNING,
        assets = mutableListOf(Cash()),
    )

    @Test
    fun `toResponse create`() {
        val response = contextCreate.toResponse() as AssetCreateResponse
        assertThat(response.requestId).isEqualTo("id")
        assertThat(response.result).isEqualTo(ResponseResult.ERROR)
        assertThat(response.errors).containsExactly(
            Error("code", "group", "id", "Bad id"),
        )
        assertThat(response.asset).isNotNull
    }

    @Test
    fun `toResponse read`() {
        val response = contextRead.toResponse() as AssetReadResponse
        assertThat(response.requestId).isEqualTo("id")
        assertThat(response.result).isEqualTo(ResponseResult.SUCCESS)
        assertThat(response.errors).isNull()
        assertThat(response.asset).isNotNull
    }

    @Test
    fun `toResponse update`() {
        val response = contextUpdate.toResponse() as AssetUpdateResponse
        assertThat(response.requestId).isEqualTo("id")
        assertThat(response.result).isEqualTo(ResponseResult.SUCCESS)
        assertThat(response.errors).isNull()
        assertThat(response.asset).isNotNull
    }

    @Test
    fun `toResponse delete`() {
        val response = contextDelete.toResponse() as AssetDeleteResponse
        assertThat(response.requestId).isEqualTo("id")
        assertThat(response.result).isEqualTo(ResponseResult.SUCCESS)
        assertThat(response.errors).isNull()
        assertThat(response.asset).isNotNull
    }

    @Test
    fun `toResponse search`() {
        val response = contextSearch.toResponse() as AssetSearchResponse
        assertThat(response.requestId).isEqualTo("id")
        assertThat(response.result).isEqualTo(ResponseResult.SUCCESS)
        assertThat(response.errors).isNull()
        assertThat(response.assets).isNotEmpty
    }
}