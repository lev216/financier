package ru.otus.otuskotlin.financier.asset.business.stub

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import ru.otus.otuskotlin.financier.asset.business.processor.AssetProcessor
import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.*
import java.math.BigDecimal
import java.time.LocalDate

class AssetUpdateStubTest {

    private val processor = AssetProcessor()

    @Test
    fun updateCash() = runTest {
        val context = AssetContext(
            command = AssetCommand.UPDATE,
            state = AssetState.NONE,
            workMode = AssetWorkMode.STUB,
            stubCase = AssetStub.SUCCESS,
            assetRequest = Cash(
                id = AssetId("da5db9d8-b13d-4094-959e-2fc57482ae70"),
                sum = BigDecimal.ONE,
                currency = "USD",
                userId = UserId("userId"),
            ),
        )

        processor.exec(context)

        with(context.assetResponse) {
            assertThat(id.asString()).isEqualTo("da5db9d8-b13d-4094-959e-2fc57482ae70")
            assertThat(sum).isEqualTo(BigDecimal.ONE)
            assertThat(currency).isEqualTo("USD")
            assertThat(userId.asString()).isEqualTo("userId")
        }
        assertThat(context.errors).isEmpty()
        assertThat(context.state).isEqualTo(AssetState.FINISHING)
    }

    @Test
    fun updateDeposit() = runTest {
        val context = AssetContext(
            command = AssetCommand.UPDATE,
            state = AssetState.NONE,
            workMode = AssetWorkMode.STUB,
            stubCase = AssetStub.SUCCESS,
            assetRequest = Deposit(
                id = AssetId("da5db9d8-b13d-4094-959e-2fc57482ae70"),
                sum = BigDecimal.ONE,
                currency = "USD",
                userId = UserId("userId"),
                startDate = LocalDate.of(2024, 1, 4),
                endDate = LocalDate.of(3034, 10, 24),
                interestRate = BigDecimal.TEN,
            ),
        )

        processor.exec(context)

        with(context.assetResponse as Deposit) {
            assertThat(id).isEqualTo(AssetId("da5db9d8-b13d-4094-959e-2fc57482ae70"))
            assertThat(sum).isEqualTo(BigDecimal.ONE)
            assertThat(currency).isEqualTo("USD")
            assertThat(userId).isEqualTo(UserId("userId"))
            assertThat(startDate).isEqualTo(LocalDate.of(2024, 1, 4))
            assertThat(endDate).isEqualTo(LocalDate.of(3034, 10, 24))
            assertThat(interestRate).isEqualTo(BigDecimal.TEN)
        }
        assertThat(context.errors).isEmpty()
        assertThat(context.state).isEqualTo(AssetState.FINISHING)
    }

    @Test
    fun badIdCash() = runTest {
        val context = AssetContext(
            command = AssetCommand.UPDATE,
            state = AssetState.NONE,
            workMode = AssetWorkMode.STUB,
            stubCase = AssetStub.BAD_ID,
            assetRequest = Cash(),
        )
        processor.exec(context)
        assertThat(context.errors).isNotEmpty
        assertThat(context.errors[0].code).isEqualTo("empty")
        assertThat(context.errors[0].field).isEqualTo("id")
        assertThat(context.state).isEqualTo(AssetState.FAILING)
    }

    @Test
    fun badIdDeposit() = runTest {
        val context = AssetContext(
            command = AssetCommand.UPDATE,
            state = AssetState.NONE,
            workMode = AssetWorkMode.STUB,
            stubCase = AssetStub.BAD_ID,
            assetRequest = Deposit(
                startDate = LocalDate.of(2024, 1, 4),
                endDate = LocalDate.of(3034, 10, 24),
                interestRate = BigDecimal.TEN,
            ),
        )
        processor.exec(context)
        assertThat(context.errors).isNotEmpty
        assertThat(context.errors[0].code).isEqualTo("empty")
        assertThat(context.errors[0].field).isEqualTo("id")
        assertThat(context.state).isEqualTo(AssetState.FAILING)
    }

    @Test
    fun negativeSumCash() = runTest {
        val context = AssetContext(
            command = AssetCommand.UPDATE,
            state = AssetState.NONE,
            workMode = AssetWorkMode.STUB,
            stubCase = AssetStub.NEGATIVE_SUM,
            assetRequest = Cash(
                sum = BigDecimal.valueOf(-1L),
            ),
        )
        processor.exec(context)
        assertThat(context.assetResponse).isEqualTo(Cash())
        assertThat(context.errors).isNotEmpty
        assertThat(context.errors[0].code).isEqualTo("negative")
        assertThat(context.errors[0].field).isEqualTo("sum")
        assertThat(context.state).isEqualTo(AssetState.FAILING)
    }

    @Test
    fun negativeSumDeposit() = runTest {
        val context = AssetContext(
            command = AssetCommand.UPDATE,
            state = AssetState.NONE,
            workMode = AssetWorkMode.STUB,
            stubCase = AssetStub.NEGATIVE_SUM,
            assetRequest = Deposit(
                sum = BigDecimal.valueOf(-1L),
                startDate = LocalDate.of(2024, 1, 4),
                endDate = LocalDate.of(3034, 10, 24),
                interestRate = BigDecimal.TEN,
            ),
        )
        processor.exec(context)
        assertThat(context.assetResponse).isEqualTo(Cash())
        assertThat(context.errors).isNotEmpty
        assertThat(context.errors[0].code).isEqualTo("negative")
        assertThat(context.errors[0].field).isEqualTo("sum")
        assertThat(context.state).isEqualTo(AssetState.FAILING)
    }

    @Test
    fun dataBaseError() = runTest {
        val context = AssetContext(
            command = AssetCommand.UPDATE,
            state = AssetState.NONE,
            workMode = AssetWorkMode.STUB,
            stubCase = AssetStub.DB_ERROR,
            assetRequest = Cash(),
        )
        processor.exec(context)
        assertThat(context.errors).isNotEmpty
        assertThat(context.errors[0].code).isEqualTo("internal-db")
        assertThat(context.state).isEqualTo(AssetState.FAILING)
    }

    @Test
    fun noCase() = runTest {
        val context = AssetContext(
            command = AssetCommand.UPDATE,
            state = AssetState.NONE,
            workMode = AssetWorkMode.STUB,
            stubCase = AssetStub.NOT_FOUND,
            assetRequest = Cash(),
        )
        processor.exec(context)
        assertThat(context.errors).isNotEmpty
        assertThat(context.errors[0].code).isEqualTo("validation")
        assertThat(context.errors[0].field).isEqualTo("stub")
        assertThat(context.state).isEqualTo(AssetState.FAILING)
    }
}