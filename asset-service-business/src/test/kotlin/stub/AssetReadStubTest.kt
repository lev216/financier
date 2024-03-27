package ru.otus.otuskotlin.financier.asset.business.stub

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import ru.otus.otuskotlin.financier.asset.business.processor.AssetProcessor
import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.*
import java.math.BigDecimal.ONE

class AssetReadStubTest {

    private val processor = AssetProcessor()

    @Test
    fun read() = runTest {
        val context = AssetContext(
            command = AssetCommand.READ,
            state = AssetState.NONE,
            workMode = AssetWorkMode.STUB,
            stubCase = AssetStub.SUCCESS,
            assetIdRequest = AssetId("da5db9d8-b13d-4094-959e-2fc57482ae70"),
        )

        processor.exec(context)

        with(context.assetResponse) {
            assertThat(id.asString()).isEqualTo("da5db9d8-b13d-4094-959e-2fc57482ae70")
            assertThat(sum).isEqualTo(ONE)
            assertThat(currency).isEqualTo("USD")
            assertThat(userId.asString()).isEqualTo("userId")
        }
        assertThat(context.errors).isEmpty()
        assertThat(context.state).isEqualTo(AssetState.FINISHING)
    }

    @Test
    fun badId() = runTest {
        val context = AssetContext(
            command = AssetCommand.READ,
            state = AssetState.NONE,
            workMode = AssetWorkMode.STUB,
            stubCase = AssetStub.BAD_ID,
            assetIdRequest = AssetId(""),
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
            command = AssetCommand.READ,
            state = AssetState.NONE,
            workMode = AssetWorkMode.STUB,
            stubCase = AssetStub.DB_ERROR,
            assetIdRequest = AssetId("da5db9d8-b13d-4094-959e-2fc57482ae70"),
        )
        processor.exec(context)
        assertThat(context.errors).isNotEmpty
        assertThat(context.errors[0].code).isEqualTo("internal-db")
        assertThat(context.state).isEqualTo(AssetState.FAILING)
    }

    @Test
    fun noCase() = runTest {
        val context = AssetContext(
            command = AssetCommand.READ,
            state = AssetState.NONE,
            workMode = AssetWorkMode.STUB,
            stubCase = AssetStub.NEGATIVE_SUM,
            assetIdRequest = AssetId("da5db9d8-b13d-4094-959e-2fc57482ae70"),
        )
        processor.exec(context)
        assertThat(context.errors).isNotEmpty
        assertThat(context.errors[0].code).isEqualTo("validation")
        assertThat(context.errors[0].field).isEqualTo("stub")
        assertThat(context.state).isEqualTo(AssetState.FAILING)
    }
}