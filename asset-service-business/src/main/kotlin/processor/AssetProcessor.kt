package ru.otus.otuskotlin.financier.asset.business.processor

import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.AssetCommand.SEARCH
import ru.otus.otuskotlin.financier.asset.common.model.AssetWorkMode
import ru.otus.otuskotlin.financier.asset.stubs.AssetStubs

class AssetProcessor {
    fun exec(context: AssetContext) {
        //TODO: Rewrite when business logic is done
        require(context.workMode == AssetWorkMode.STUB) {
            "Currently working in STUB mode"
        }
        when(context.command) {
            SEARCH -> if (context.requestId.asString() == "0") {
                context.assets.addAll(AssetStubs.prepareEmptySearchList())
            } else {
                context.assets.addAll(AssetStubs.prepareFullSearchList())
            }
            else -> if (context.requestId.asString() == "0") {
                context.assetResponse = AssetStubs.getCash()
            } else {
                context.assetResponse = AssetStubs.getDeposit()
            }

        }
    }
}