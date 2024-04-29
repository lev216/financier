package ru.otus.otuskotlin.financier.asset.common

import ru.otus.otuskotlin.financier.asset.common.persistence.IAssetRepository

data class AssetCorSettings(
    val repositoryStub: IAssetRepository = IAssetRepository.NONE,
    val repositoryTest: IAssetRepository = IAssetRepository.NONE,
    val repositoryProd: IAssetRepository = IAssetRepository.NONE,
) {
    companion object {
        val NONE = AssetCorSettings()
    }
}
