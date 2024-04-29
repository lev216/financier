package ru.otus.otuskotlin.financier.asset.app.common

import ru.otus.otuskotlin.financier.asset.business.processor.AssetProcessor
import ru.otus.otuskotlin.financier.asset.common.AssetCorSettings

interface IAssetAppSettings {
    val processor: AssetProcessor
    val corSettings: AssetCorSettings
}