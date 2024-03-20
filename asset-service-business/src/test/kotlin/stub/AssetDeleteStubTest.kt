package ru.otus.otuskotlin.financier.asset.business.stub

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions
import org.junit.Test
import ru.otus.otuskotlin.financier.asset.business.processor.AssetProcessor
import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.*
import java.math.BigDecimal

class AssetDeleteStubTest {

    private val processor = AssetProcessor()

    @Test
    fun delete() = runTest {
        val context = AssetContext(
            command = AssetCommand.DELETE,
            state = AssetState.NONE,
            workMode = AssetWorkMode.STUB,
            stubCase = AssetStub.SUCCESS,
            assetIdRequest = AssetId("da5db9d8-b13d-4094-959e-2fc57482ae70"),
        )

        processor.exec(context)

        with(context.assetResponse) {
            Assertions.assertThat(id.asString()).isEqualTo("da5db9d8-b13d-4094-959e-2fc57482ae70")
            Assertions.assertThat(sum).isEqualTo(BigDecimal.ONE)
            Assertions.assertThat(currency).isEqualTo("USD")
            Assertions.assertThat(userId.asString()).isEqualTo("userId")
        }
        Assertions.assertThat(context.errors).isEmpty()
        Assertions.assertThat(context.state).isEqualTo(AssetState.FINISHING)
    }

    @Test
    fun badId() = runTest {
        val context = AssetContext(
            command = AssetCommand.DELETE,
            state = AssetState.NONE,
            workMode = AssetWorkMode.STUB,
            stubCase = AssetStub.BAD_ID,
            assetIdRequest = AssetId(""),
        )
        processor.exec(context)
        Assertions.assertThat(context.errors).isNotEmpty
        Assertions.assertThat(context.errors[0].code).isEqualTo("empty")
        Assertions.assertThat(context.errors[0].field).isEqualTo("id")
        Assertions.assertThat(context.state).isEqualTo(AssetState.FAILING)
    }

    @Test
    fun dataBaseError() = runTest {
        val context = AssetContext(
            command = AssetCommand.DELETE,
            state = AssetState.NONE,
            workMode = AssetWorkMode.STUB,
            stubCase = AssetStub.DB_ERROR,
            assetIdRequest = AssetId("da5db9d8-b13d-4094-959e-2fc57482ae70"),
        )
        processor.exec(context)
        Assertions.assertThat(context.errors).isNotEmpty
        Assertions.assertThat(context.errors[0].code).isEqualTo("internal-db")
        Assertions.assertThat(context.state).isEqualTo(AssetState.FAILING)
    }

    @Test
    fun noCase() = runTest {
        val context = AssetContext(
            command = AssetCommand.DELETE,
            state = AssetState.NONE,
            workMode = AssetWorkMode.STUB,
            stubCase = AssetStub.NEGATIVE_SUM,
            assetIdRequest = AssetId("da5db9d8-b13d-4094-959e-2fc57482ae70"),
        )
        processor.exec(context)
        Assertions.assertThat(context.errors).isNotEmpty
        Assertions.assertThat(context.errors[0].code).isEqualTo("validation")
        Assertions.assertThat(context.errors[0].field).isEqualTo("stub")
        Assertions.assertThat(context.state).isEqualTo(AssetState.FAILING)
    }
}