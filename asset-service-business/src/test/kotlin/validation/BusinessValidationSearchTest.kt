package ru.otus.otuskotlin.financier.asset.business.validation

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import ru.otus.otuskotlin.financier.asset.business.processor.AssetProcessor
import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.AssetCommand
import ru.otus.otuskotlin.financier.asset.common.model.AssetFilter
import ru.otus.otuskotlin.financier.asset.common.model.AssetState
import ru.otus.otuskotlin.financier.asset.common.model.AssetWorkMode

class BusinessValidationSearchTest {

    private val command = AssetCommand.SEARCH
    private val processor by lazy { AssetProcessor() }

    @Test
    fun correctSearch() = runTest {
        val context = AssetContext(
            command = command,
            state = AssetState.NONE,
            workMode = AssetWorkMode.TEST,
            assetFilterRequest = AssetFilter(),
        )

        processor.exec(context)

        assertThat(context.errors).isEmpty()
        assertThat(context.state).isNotEqualTo(AssetState.FAILING)
    }
}