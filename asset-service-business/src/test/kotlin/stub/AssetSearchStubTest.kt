package ru.otus.otuskotlin.financier.asset.business.stub

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import ru.otus.otuskotlin.financier.asset.business.processor.AssetProcessor
import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.*
import java.math.BigDecimal
import java.time.LocalDate

class AssetSearchStubTest {

    private val processor = AssetProcessor()

    @Test
    fun search() = runTest {
        val context = AssetContext(
            command = AssetCommand.SEARCH,
            state = AssetState.NONE,
            workMode = AssetWorkMode.STUB,
            stubCase = AssetStub.SUCCESS,
            assetFilterRequest = AssetFilter(
                userId = UserId("userId"),
            ),
        )

        processor.exec(context)

        assertThat(context.assetsResponse).containsExactlyInAnyOrder(
            Cash(
                id = AssetId("da5db9d8-b13d-4094-959e-2fc57482ae70"),
                sum = BigDecimal.ONE,
                currency = "USD",
                userId = UserId("userId"),
            ),
            Deposit(
                id = AssetId("da5db9d8-b13d-4094-959e-2fc57482ae70"),
                sum = BigDecimal.ONE,
                currency = "USD",
                userId = UserId("userId"),
                startDate = LocalDate.of(2024, 1, 4),
                endDate = LocalDate.of(3034, 10, 24),
                interestRate = BigDecimal.TEN,
            ),
        )
        assertThat(context.errors).isEmpty()
        assertThat(context.state).isEqualTo(AssetState.FINISHING)
    }

    @Test
    fun badId() = runTest {
        val context = AssetContext(
            command = AssetCommand.SEARCH,
            state = AssetState.NONE,
            workMode = AssetWorkMode.STUB,
            stubCase = AssetStub.BAD_ID,
            assetFilterRequest = AssetFilter(
                id = AssetId(""),
            ),
        )
        processor.exec(context)
        assertThat(context.errors).isNotEmpty
        assertThat(context.errors[0].code).isEqualTo("empty")
        assertThat(context.errors[0].field).isEqualTo("id")
        assertThat(context.state).isEqualTo(AssetState.FAILING)
    }

    @Test
    fun dataBaseError() = runTest {
        val context = AssetContext(
            command = AssetCommand.SEARCH,
            state = AssetState.NONE,
            workMode = AssetWorkMode.STUB,
            stubCase = AssetStub.DB_ERROR,
            assetFilterRequest = AssetFilter(),
        )
        processor.exec(context)
        assertThat(context.errors).isNotEmpty
        assertThat(context.errors[0].code).isEqualTo("internal-db")
        assertThat(context.state).isEqualTo(AssetState.FAILING)
    }

    @Test
    fun noCase() = runTest {
        val context = AssetContext(
            command = AssetCommand.SEARCH,
            state = AssetState.NONE,
            workMode = AssetWorkMode.STUB,
            stubCase = AssetStub.NEGATIVE_SUM,
            assetFilterRequest = AssetFilter(),
        )
        processor.exec(context)
        assertThat(context.errors).isNotEmpty
        assertThat(context.errors[0].code).isEqualTo("validation")
        assertThat(context.errors[0].field).isEqualTo("stub")
        assertThat(context.state).isEqualTo(AssetState.FAILING)
    }
}