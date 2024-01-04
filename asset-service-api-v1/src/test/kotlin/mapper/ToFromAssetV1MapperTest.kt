package ru.otus.otuskotlin.financier.asset.api.v1.mapper

import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.Test
import ru.otus.otuskotlin.financier.asset.api.v1.FakeAssets.cash
import ru.otus.otuskotlin.financier.asset.api.v1.FakeAssets.deposit
import ru.otus.otuskotlin.financier.asset.api.v1.models.*
import ru.otus.otuskotlin.financier.asset.api.v1.models.AssetType.*
import ru.otus.otuskotlin.financier.asset.common.model.Asset
import java.math.BigDecimal
import java.math.BigDecimal.*
import java.time.LocalDateTime
import java.util.UUID

class ToFromAssetV1MapperTest {

    private val uuid = UUID.fromString("da5db9d8-b13d-4094-959e-2fc57482ae70")
    private val startDate = LocalDateTime.of(2024, 1, 4, 19, 27)
    private val endDate = LocalDateTime.of(3034, 10, 24, 9, 7)
    private val createRequestForCash = AssetCreateRequest(
        "create",
        "requestId",
        AssetCreateObject(
            ONE,
            "USD",
            "userId",
            CASH,
        )
    )
    private val createRequestForDeposit = AssetCreateRequest(
        "create",
        "requestId",
        AssetCreateObject(
            ONE,
            "USD",
            "userId",
            DEPOSIT,
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
            CASH,
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
            DEPOSIT,
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
        type = CASH,
        id = uuid.toString(),
    )
    private val assetResponseDeposit = AssetResponseObject(
        sum = ONE,
        currency = "USD",
        userId = "userId",
        type = DEPOSIT,
        depositFields = DepositFields(
            startDate.toString(),
            endDate.toString(),
            TEN,
        ),
        id = uuid.toString(),
    )

    @Test
    fun `mapToAsset with request to create asset with type CASH`() {
        val expected = cash
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns uuid

        val actual = mapToAsset(createRequestForCash)
        assertThat(actual).isEqualTo(expected)

        unmockkStatic(UUID::class)
    }

    @Test
    fun `mapToAsset with request to create asset with type DEPOSIT`() {
        val expected = deposit
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns uuid

        val actual = mapToAsset(createRequestForDeposit)
        assertThat(actual).isEqualTo(expected)

        unmockkStatic(UUID::class)
    }

    @Test
    fun `mapToAsset with request to update asset with type CASH`() {
        val expected = cash

        val actual = mapToAsset(updateRequestForCash)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `mapToAsset with request to update asset with type DEPOSIT`() {
        val expected = deposit

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
                DEPOSIT,
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
        val cash = cash

        val actual = mapFromAsset(cash)
        assertThat(actual).isEqualTo(assetResponseCash)
    }

    @Test
    fun `mapFromAsset with asset of type DEPOSIT`() {
        val cash = deposit

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