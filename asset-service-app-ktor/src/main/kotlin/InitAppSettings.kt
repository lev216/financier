package ru.otus.otuskotlin.financier.asset

import ru.otus.otuskotlin.financier.asset.common.AssetCorSettings
import ru.otus.otuskotlin.financier.asset.persistence.inmemory.stubs.AssetRepositoryStub

fun Config.initAppSettings() = AssetAppSettings(
  corSettings = AssetCorSettings(
    repositoryStub = AssetRepositoryStub(),
    repositoryTest = getDatabaseConf(AssetDbType.TEST),
    repositoryProd = getDatabaseConf(AssetDbType.PROD),
  )
)