package ru.otus.otuskotlin.financier.asset.business.validation

import org.junit.Test
import ru.otus.otuskotlin.financier.asset.business.processor.AssetProcessor
import ru.otus.otuskotlin.financier.asset.common.AssetCorSettings
import ru.otus.otuskotlin.financier.asset.common.model.AssetCommand
import ru.otus.otuskotlin.financier.asset.persistence.inmemory.stubs.AssetRepositoryStub

class BusinessValidationReadTest {

    private val command = AssetCommand.READ
    private val settings by lazy {
        AssetCorSettings(repositoryTest = AssetRepositoryStub())
    }
    private val processor by lazy { AssetProcessor(settings) }

    @Test
    fun correctId() = validationIdCorrect(command, processor)

    @Test
    fun trimId() = validationIdTrim(command, processor)

    @Test
    fun idIsEmpty() = validationIdEmpty(command, processor)

    @Test
    fun idBadFormat() = validationIdFormat(command, processor)
}