package ru.otus.otuskotlin.financier.asset.persistence.inmemory

import ru.otus.otuskotlin.financier.asset.common.persistence.IAssetRepository
import ru.otus.otuskotlin.financier.asset.persistence.tests.PersistAssetSearchTest

class AssetRepositoryInMemorySearchTest : PersistAssetSearchTest() {
    override val repository: IAssetRepository = AssetRepositoryInMemory(
        initObjects = initObjects,
    )
}