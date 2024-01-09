package mapper

import fakeCash
import fakeDeposit
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.Test
import ru.otus.otuskotlin.financier.asset.api.v1.models.*
import ru.otus.otuskotlin.financier.asset.mapper.mapFromAsset
import ru.otus.otuskotlin.financier.asset.mapper.mapToAsset
import ru.otus.otuskotlin.financier.asset.model.Asset
import java.math.BigDecimal
import java.math.BigDecimal.*
import java.time.LocalDateTime
import java.util.UUID

class ToFromAssetV1MapperTest {

    private val uuid = UUID.fromString("da5db9d8-b13d-4094-959e-2fc57482ae70")
    private val startDate = LocalDateTime.of(2024, 1, 4, 19, 27)
    private val endDate = LocalDateTime.of(2034, 10, 24, 9, 7)
    private val createRequestForCash = AssetCreateRequest(
        "create",
        "requestId",
        AssetCreateObject(
            ONE,
            "USD",
            "userId",
            AssetCreateObject.Type.CASH,
        )
    )
    private val createRequestForDeposit = AssetCreateRequest(
        "create",
        "requestId",
        AssetCreateObject(
            ONE,
            "USD",
            "userId",
            AssetCreateObject.Type.DEPOSIT,
            DepositFields(
                startDate.toString(),
                endDate.toString(),
                TEN,
            ),
        )
    )
    private val updateRequestForCash = AssetUpdateRequest(
        "create",
        "requestId",
        AssetUpdateObject(
            ONE,
            "USD",
            "userId",
            AssetUpdateObject.Type.CASH,
            uuid.toString(),
        )
    )
    private val updateRequestForDeposit = AssetUpdateRequest(
        "create",
        "requestId",
        AssetUpdateObject(
            ONE,
            "USD",
            "userId",
            AssetUpdateObject.Type.DEPOSIT,
            uuid.toString(),
            DepositFields(
                startDate.toString(),
                endDate.toString(),
                TEN,
            ),
        )
    )
    private val assetResponseCash = AssetResponseObject(
        sum = ONE,
        currency = "USD",
        userId = "userId",
        type = AssetResponseObject.Type.CASH,
        id = uuid.toString(),
    )
    private val assetResponseDeposit = AssetResponseObject(
        sum = ONE,
        currency = "USD",
        userId = "userId",
        type = AssetResponseObject.Type.DEPOSIT,
        depositFields = DepositFields(
            startDate.toString(),
            endDate.toString(),
            TEN,
        ),
        id = uuid.toString(),
    )

    @Test
    fun `mapToAsset with request to create asset with type CASH`() {
        val expected = fakeCash(id = "da5db9d8-b13d-4094-959e-2fc57482ae70")
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns uuid

        val actual = mapToAsset(createRequestForCash)
        assertThat(actual).isEqualTo(expected)

        unmockkStatic(UUID::class)
    }

    @Test
    fun `mapToAsset with request to create asset with type DEPOSIT`() {
        val expected = fakeDeposit(
            id = "da5db9d8-b13d-4094-959e-2fc57482ae70",
            startDate = startDate,
            endDate = endDate,
        )
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns uuid

        val actual = mapToAsset(createRequestForDeposit)
        assertThat(actual).isEqualTo(expected)

        unmockkStatic(UUID::class)
    }

    @Test
    fun `mapToAsset with request to update asset with type CASH`() {
        val expected = fakeCash(id = "da5db9d8-b13d-4094-959e-2fc57482ae70")

        val actual = mapToAsset(updateRequestForCash)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `mapToAsset with request to update asset with type DEPOSIT`() {
        val expected = fakeDeposit(
            id = "da5db9d8-b13d-4094-959e-2fc57482ae70",
            startDate = startDate,
            endDate = endDate,
        )

        val actual = mapToAsset(updateRequestForDeposit)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `mapToAsset without deposit data`() {
        val requestWithoutDepositData = AssetCreateRequest(
            "create",
            "requestId",
            AssetCreateObject(
                ONE,
                "USD",
                "userId",
                AssetCreateObject.Type.DEPOSIT,
                null,
            )
        )

        assertThatCode { mapToAsset(requestWithoutDepositData) }
            .isInstanceOf(NullPointerException::class.java)
            .hasMessage("text")
    }

    @Test
    fun `mapToAsset when asset can't be mapped`() {
        val assetDeleteRequest = AssetDeleteRequest(
            "delete",
            "requestId",
            AssetDeleteObject(
                "id"
            )
        )

        assertThatCode { mapToAsset(assetDeleteRequest) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Asset can't be mapped from request: AssetDeleteRequest(requestType=delete, requestId=requestId, asset=AssetDeleteObject(id=id, lock=null), debug=null)")
    }

    @Test
    fun `mapFromAsset with asset of type CASH`() {
        val cash = fakeCash(id = "da5db9d8-b13d-4094-959e-2fc57482ae70")

        val actual = mapFromAsset(cash)
        assertThat(actual).isEqualTo(assetResponseCash)
    }

    @Test
    fun `mapFromAsset with asset of type DEPOSIT`() {
        val cash = fakeDeposit(
            id = "da5db9d8-b13d-4094-959e-2fc57482ae70",
            startDate = startDate,
            endDate = endDate,
        )

        val actual = mapFromAsset(cash)
        assertThat(actual).isEqualTo(assetResponseDeposit)
    }

    @Test
    fun `mapFromAsset unknown asset`() {
        val asset = object : Asset {
            override val id: String = "id"
            override val sum: BigDecimal = ZERO
            override val currency: String = "currency"
            override val userId: String = "userId"
            override fun toString() = "Strange asset"
        }

        assertThatCode { mapFromAsset(asset) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Asset is unknown: Strange asset")
    }
}