package ru.otus.otuskotlin.financier.asset

import ru.otus.otuskotlin.financier.asset.app.common.IAssetAppSettings
import ru.otus.otuskotlin.financier.asset.business.processor.AssetProcessor
import ru.otus.otuskotlin.financier.asset.common.AssetCorSettings

data class AssetAppSettings(
    override val corSettings: AssetCorSettings = AssetCorSettings.NONE,
    override val processor: AssetProcessor = AssetProcessor(corSettings),
) : IAssetAppSettings
