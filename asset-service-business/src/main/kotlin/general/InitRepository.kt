package ru.otus.otuskotlin.financier.asset.business.general

import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.helper.errorAdministration
import ru.otus.otuskotlin.financier.asset.common.helper.fail
import ru.otus.otuskotlin.financier.asset.common.model.AssetWorkMode
import ru.otus.otuskotlin.financier.asset.common.persistence.IAssetRepository
import ru.otus.otuskotlin.financier.cor.ICorChainDsl
import ru.otus.otuskotlin.financier.cor.worker

fun ICorChainDsl<AssetContext>.initRepository(title: String) = worker {
    this.title = title
    description = "Computing repository type according to requested work mode"
    handle {
        assetRepository = when(workMode) {
            AssetWorkMode.STUB -> corSettings.repositoryStub
            AssetWorkMode.TEST -> corSettings.repositoryTest
            AssetWorkMode.PROD -> corSettings.repositoryProd
        }
        if (workMode != AssetWorkMode.STUB && assetRepository == IAssetRepository.NONE) fail(
            errorAdministration(
                field = "repo",
                violationCode = "dbNotConfigured",
                description = "The database isn't configured for chosen work mode ($workMode)."
            )
        )
    }
}