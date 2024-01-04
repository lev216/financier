package mapper

import fakeCash
import fakeDeposit
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.Test
import ru.otus.otuskotlin.financier.asset.api.v1.models.CreateUpdateAssetRequest
import ru.otus.otuskotlin.financier.asset.api.v1.models.CreateUpdateAssetRequest.Type.CASH
import ru.otus.otuskotlin.financier.asset.api.v1.models.CreateUpdateAssetRequest.Type.DEPOSIT
import ru.otus.otuskotlin.financier.asset.api.v1.models.DepositFields
import ru.otus.otuskotlin.financier.asset.api.v1.models.FoundAsset
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
    private val requestForCash = CreateUpdateAssetRequest(
        ONE,
        "USD",
        "userId",
        CASH,
    )
    private val requestForDeposit = CreateUpdateAssetRequest(
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
    private val foundAssetCash = FoundAsset(
        uuid.toString(),
        ONE,
        "USD",
        "userId",
        FoundAsset.Type.CASH,
    )
    private val foundAssetDeposit = FoundAsset(
        uuid.toString(),
        ONE,
        "USD",
        "userId",
        FoundAsset.Type.DEPOSIT,
        DepositFields(
            startDate.toString(),
            endDate.toString(),
            TEN,
        ),
    )

    @Test
    fun `mapToAsset with request to create asset with type CASH`() {
        val expected = fakeCash(id = "da5db9d8-b13d-4094-959e-2fc57482ae70")
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns uuid

        val actual = mapToAsset(requestForCash, null)
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

        val actual = mapToAsset(requestForDeposit, null)
        assertThat(actual).isEqualTo(expected)

        unmockkStatic(UUID::class)
    }

    @Test
    fun `mapToAsset with request to update asset with type CASH`() {
        val expected = fakeCash(id = "da5db9d8-b13d-4094-959e-2fc57482ae70")

        val actual = mapToAsset(requestForCash, uuid.toString())
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `mapToAsset with request to update asset with type DEPOSIT`() {
        val expected = fakeDeposit(
            id = "da5db9d8-b13d-4094-959e-2fc57482ae70",
            startDate = startDate,
            endDate = endDate,
        )

        val actual = mapToAsset(requestForDeposit, uuid.toString())
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `mapToAsset without deposit data`() {
        val requestWithoutDepositData = CreateUpdateAssetRequest(
            ONE,
            "USD",
            "userId",
            DEPOSIT,
            null,
        )

        assertThatCode { mapToAsset(requestWithoutDepositData, null) }
            .isInstanceOf(NullPointerException::class.java)
            .hasMessage("text")
    }

    @Test
    fun `mapFromAsset with asset of type CASH`() {
        val cash = fakeCash(id = "da5db9d8-b13d-4094-959e-2fc57482ae70")

        val actual = mapFromAsset(cash)
        assertThat(actual).isEqualTo(foundAssetCash)
    }

    @Test
    fun `mapFromAsset with asset of type DEPOSIT`() {
        val cash = fakeDeposit(
            id = "da5db9d8-b13d-4094-959e-2fc57482ae70",
            startDate = startDate,
            endDate = endDate,
        )

        val actual = mapFromAsset(cash)
        assertThat(actual).isEqualTo(foundAssetDeposit)
    }

    @Test
    fun `mapFromAsset unknown asset`() {
        val asset = object : Asset {
            override val id: String = "id"
            override val sum: BigDecimal = ZERO
            override val currency: String = "currency"
            override val userId: String = "userId"
        }

        assertThatCode { mapFromAsset(asset) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Asset is unknown")
    }
}