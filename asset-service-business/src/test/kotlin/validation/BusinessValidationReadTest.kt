package ru.otus.otuskotlin.financier.asset.business.validation

import org.junit.Test
import ru.otus.otuskotlin.financier.asset.business.processor.AssetProcessor
import ru.otus.otuskotlin.financier.asset.common.model.AssetCommand

class BusinessValidationReadTest {

    private val command = AssetCommand.READ
    private val processor by lazy { AssetProcessor() }

    @Test
    fun correctId() = validationIdCorrect(command, processor)

    @Test
    fun trimId() = validationIdTrim(command, processor)

    @Test
    fun idIsEmpty() = validationIdEmpty(command, processor)

    @Test
    fun idBadFormat() = validationIdFormat(command, processor)
}