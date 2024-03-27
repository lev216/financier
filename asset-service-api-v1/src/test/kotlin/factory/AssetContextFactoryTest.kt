package ru.otus.otuskotlin.financier.asset.api.v1.factory

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import ru.otus.otuskotlin.financier.asset.api.v1.models.*
import ru.otus.otuskotlin.financier.asset.common.model.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class AssetContextFactoryTest {
    companion object {
        private const val REQUEST_ID = "requestId"
    }
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    private val uuid = UUID.fromString("da5db9d8-b13d-4094-959e-2fc57482ae70")
    private val startDate = LocalDate.now().minusMonths(2)
    private val endDate = startDate.plusYears(10)
    private val createRequest = AssetCreateRequest(
        "create",
        REQUEST_ID,
        AssetCreateObject(
            BigDecimal.ONE,
            "USD",
            "userId",
            AssetType.CASH,
        ),
        AssetRequestDebug(
            AssetDebug(
                AssetRequestDebugMode.STUB,
                AssetRequestDebugStubs.NEGATIVE_SUM,
            )
        ),
    )
    private val updateRequest = AssetUpdateRequest(
        "update",
        REQUEST_ID,
        AssetUpdateObject(
            BigDecimal.ONE,
            "USD",
            "userId",
            AssetType.CASH,
            uuid.toString(),
        ),
        AssetRequestDebug(
            AssetDebug(
                AssetRequestDebugMode.TEST,
                AssetRequestDebugStubs.BAD_ID,
            )
        ),
    )
    private val readRequest = AssetReadRequest(
        "read",
        REQUEST_ID,
        AssetReadObject(
            uuid.toString(),
        ),
        AssetRequestDebug(
            AssetDebug(
                AssetRequestDebugMode.PROD,
                AssetRequestDebugStubs.NOT_FOUND,
            )
        ),
    )
    private val deleteRequest = AssetDeleteRequest(
        "delete",
        REQUEST_ID,
        AssetDeleteObject(
            uuid.toString(),
        ),
        AssetRequestDebug(
            AssetDebug(
                AssetRequestDebugMode.PROD,
                AssetRequestDebugStubs.CANNOT_DELETE,
            )
        ),
    )
    private val searchRequest = AssetSearchRequest(
        "search",
        REQUEST_ID,
        AssetSearchFilter(
            uuid.toString(),
            startDate.format(dateTimeFormatter),
            endDate.format(dateTimeFormatter),
            "userId",
            AssetType.DEPOSIT,
        ),
        AssetRequestDebug(
            AssetDebug(
                AssetRequestDebugMode.PROD,
                AssetRequestDebugStubs.SUCCESS,
            )
        ),
    )

    @Test
    fun `createContext with request to create asset`() {
        val context = AssetContextFactory.createContext(createRequest)
        assertThat(context.command).isEqualTo(AssetCommand.CREATE)
        assertThat(context.workMode).isEqualTo(AssetWorkMode.STUB)
        assertThat(context.state).isEqualTo(AssetState.RUNNING)
        assertThat(context.stubCase).isEqualTo(AssetStub.NEGATIVE_SUM)
        assertThat(context.requestId).isEqualTo(AssetRequestId(REQUEST_ID))
        assertThat(context.timeStart).isNotNull
        assertThat(context.assetRequest).isNotNull
        assertThat(context.assetIdRequest).isEqualTo(AssetId.NONE)
        assertThat(context.assetFilterRequest).isEqualTo(AssetFilter())
    }

    @Test
    fun `createContext with request to update asset`() {
        val context = AssetContextFactory.createContext(updateRequest)
        assertThat(context.command).isEqualTo(AssetCommand.UPDATE)
        assertThat(context.workMode).isEqualTo(AssetWorkMode.TEST)
        assertThat(context.state).isEqualTo(AssetState.RUNNING)
        assertThat(context.stubCase).isEqualTo(AssetStub.BAD_ID)
        assertThat(context.requestId).isEqualTo(AssetRequestId(REQUEST_ID))
        assertThat(context.timeStart).isNotNull
        assertThat(context.assetRequest).isNotNull
        assertThat(context.assetIdRequest).isEqualTo(AssetId.NONE)
        assertThat(context.assetFilterRequest).isEqualTo(AssetFilter())
    }

    @Test
    fun `createContext with request to read asset`() {
        val context = AssetContextFactory.createContext(readRequest)
        assertThat(context.command).isEqualTo(AssetCommand.READ)
        assertThat(context.workMode).isEqualTo(AssetWorkMode.PROD)
        assertThat(context.state).isEqualTo(AssetState.RUNNING)
        assertThat(context.stubCase).isEqualTo(AssetStub.NOT_FOUND)
        assertThat(context.requestId).isEqualTo(AssetRequestId(REQUEST_ID))
        assertThat(context.timeStart).isNotNull
        assertThat(context.assetRequest).isNotNull
        assertThat(context.assetIdRequest).isEqualTo(AssetId(uuid.toString()))
        assertThat(context.assetFilterRequest).isEqualTo(AssetFilter())
    }

    @Test
    fun `createContext with request to delete asset`() {
        val context = AssetContextFactory.createContext(deleteRequest)
        assertThat(context.command).isEqualTo(AssetCommand.DELETE)
        assertThat(context.workMode).isEqualTo(AssetWorkMode.PROD)
        assertThat(context.state).isEqualTo(AssetState.RUNNING)
        assertThat(context.stubCase).isEqualTo(AssetStub.CANNOT_DELETE)
        assertThat(context.requestId).isEqualTo(AssetRequestId(REQUEST_ID))
        assertThat(context.timeStart).isNotNull
        assertThat(context.assetRequest).isNotNull
        assertThat(context.assetIdRequest).isEqualTo(AssetId(uuid.toString()))
        assertThat(context.assetFilterRequest).isEqualTo(AssetFilter())
    }

    @Test
    fun `createContext with request to search asset`() {
        val context = AssetContextFactory.createContext(searchRequest)
        assertThat(context.command).isEqualTo(AssetCommand.SEARCH)
        assertThat(context.workMode).isEqualTo(AssetWorkMode.PROD)
        assertThat(context.state).isEqualTo(AssetState.RUNNING)
        assertThat(context.stubCase).isEqualTo(AssetStub.SUCCESS)
        assertThat(context.requestId).isEqualTo(AssetRequestId(REQUEST_ID))
        assertThat(context.timeStart).isNotNull
        assertThat(context.assetRequest).isNotNull
        assertThat(context.assetIdRequest).isEqualTo(AssetId.NONE)
        assertThat(context.assetFilterRequest).isEqualTo(AssetFilter(
            AssetId((uuid.toString())),
            startDate,
            endDate,
            UserId("userId"),
            AssetSearchType.DEPOSIT,
        ))
    }
}