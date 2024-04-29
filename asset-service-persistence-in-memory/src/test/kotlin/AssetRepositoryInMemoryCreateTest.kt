package ru.otus.otuskotlin.financier.asset.persistence.inmemory

import ru.otus.otuskotlin.financier.asset.common.persistence.IAssetRepository
import ru.otus.otuskotlin.financier.asset.persistence.tests.PersistAssetCreateTest

class AssetRepositoryInMemoryCreateTest : PersistAssetCreateTest() {
    override val repository: IAssetRepository = AssetRepositoryInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() },
    )
}