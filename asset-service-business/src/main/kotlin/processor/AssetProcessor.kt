package ru.otus.otuskotlin.financier.asset.business.processor

import ru.otus.otuskotlin.financier.asset.business.general.initRepository
import ru.otus.otuskotlin.financier.asset.business.general.prepareResult
import ru.otus.otuskotlin.financier.asset.business.group.operation
import ru.otus.otuskotlin.financier.asset.business.group.stubs
import ru.otus.otuskotlin.financier.asset.business.persistence.*
import ru.otus.otuskotlin.financier.asset.business.utils.copy
import ru.otus.otuskotlin.financier.asset.business.validation.*
import ru.otus.otuskotlin.financier.asset.business.worker.*
import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.AssetCorSettings
import ru.otus.otuskotlin.financier.asset.common.model.*
import ru.otus.otuskotlin.financier.asset.common.model.AssetCommand.*
import ru.otus.otuskotlin.financier.cor.chain
import ru.otus.otuskotlin.financier.cor.rootChain
import ru.otus.otuskotlin.financier.cor.worker

class AssetProcessor(
    private val corSettings: AssetCorSettings = AssetCorSettings.NONE,
) {
    suspend fun exec(context: AssetContext) = BusinessChain.exec(context.also { it.corSettings = corSettings })

    companion object {
        private val BusinessChain = rootChain {
            initStatus("Status initialization")
            initRepository("Repository initialization")

            operation("Asset creation", CREATE) {
                stubs("Stub processing while asset creation") {
                    stubCreateCashSuccess("Cash successful creation imitation")
                    stubCreateDepositSuccess("Deposit successful creation imitation")
                    stubValidationNegativeSum("Negative sum imitation")
                    stubDbError("DB error imitation")
                    stubNoCase("Stub is not allowed")
                }
                validation {
                    worker("Copying fields to assetValidating") { assetValidating = assetRequest.copy() }
                    worker("Cleaning id") { assetValidating.id = AssetId.NONE  }
                    validateUserIdIsNotEmpty("UserId is not empty check")
                    validateUserIdProperFormat("UserId is correct check")
                    validateCurrencyIsNotEmpty("Currency check")
                    validateSumIsPositive("Sum check")
                    validateStartDateNotInFuture("StartDate check")
                    validateEndDateIsAfterStartDate("EndDate check")
                    validateEndDateIsNotInPast("End date in future check")
                    validateInterestRateIsPositive("InterestRate check")

                    finishAssetValidation("Asset validation while creation is finished")
                }
                chain {
                    title = "Save asset in db"
                    prepareCreate("Prepare asset to save")
                    persistenceCreate("Creating asset in db")
                }
                prepareResult("Answer preparation after asset creation")
            }
            operation("Asset read", READ) {
                stubs("Stub processing while asset reading") {
                    stubReadCashSuccess("Cash successful reading imitation")
                    stubValidationBadId("Bad id imitation")
                    stubDbError("DB error imitation")
                    stubNoCase("Stub is not allowed")
                }
                validation {
                    worker("Copying id to assetIdValidating") { assetIdValidating = AssetId(assetIdRequest.asString().trim()) }
                    validateAssetIdIsNotEmpty("AssetId is not empty check")
                    validateAssetIdProperFormat("AssetId is correct check")

                    finishAssetIdValidation("AssetId validation while reading is finished")
                }
                chain {
                    title = "Read asset from db"
                    persistenceRead("Reading asset from db")
                    worker {
                        title = "Finish reading asset"
                        on { state == AssetState.RUNNING }
                        handle { assetPersistentDone = assetPersistentRead }
                    }
                }
                prepareResult("Answer preparation after asset reading")
            }
            operation("Asset update", UPDATE) {
                stubs("Stub processing while asset update") {
                    stubUpdateCashSuccess("Cash successful update imitation")
                    stubUpdateDepositSuccess("Deposit successful update imitation")
                    stubValidationBadId("Bad id imitation")
                    stubValidationNegativeSum("Negative sum imitation")
                    stubDbError("DB error imitation")
                    stubNoCase("Stub is not allowed")
                }
                validation {
                    worker("Copying fields to assetValidating") { assetValidating = assetRequest.copy() }
                    worker("Trimming id") { assetIdValidating = AssetId(assetValidating.id.asString().trim()) }
                    validateAssetIdIsNotEmpty("AssetId is not empty check")
                    validateAssetIdProperFormat("AssetId is correct check")
                    validateUserIdIsNotEmpty("UserId is not empty check")
                    validateUserIdProperFormat("UserId is correct check")
                    validateCurrencyIsNotEmpty("Currency check")
                    validateSumIsPositive("Sum check")
                    validateStartDateNotInFuture("StartDate check")
                    validateEndDateIsAfterStartDate("EndDate check")
                    validateEndDateIsNotInPast("End date in future check")
                    validateInterestRateIsPositive("InterestRate check")

                    finishAssetValidation("Asset validation while update is finished")
                }
                chain {
                    title = "Update asset in db"
                    persistenceRead("Reading asset from db")
                    prepareUpdate("Preparation asset to update")
                    persistenceUpdate("Updating asset in db")
                }
                prepareResult("Answer preparation after asset update")
            }
            operation("Asset delete", DELETE) {
                stubs("Stub processing while asset delete") {
                    stubDeleteCashSuccess("Cash successful delete imitation")
                    stubValidationBadId("Bad id imitation")
                    stubDbError("DB error imitation")
                    stubNoCase("Stub is not allowed")
                }
                validation {
                    worker("Copying id to assetIdValidating") { assetIdValidating = AssetId(assetIdRequest.asString().trim()) }
                    validateAssetIdIsNotEmpty("AssetId is not empty check")
                    validateAssetIdProperFormat("AssetId is correct check")

                    finishAssetIdValidation("AssetId validation while reading is finished")
                }
                chain {
                    title = "Remove asset from db"
                    persistenceRead("Reading asset from db")
                    prepareDelete("Preparation asset to delete")
                    persistenceDelete("Removing asset from db")
                }
                prepareResult("Answer preparation after asset delete")
            }
            operation("Asset search", SEARCH) {
                stubs("Stub processing while asset search") {
                    stubSearchSuccess("Assets successful search imitation")
                    stubValidationBadId("Bad id imitation")
                    stubDbError("DB error imitation")
                    stubNoCase("Stub is not allowed")
                }
                validation {
                    worker("Copying fields to assetFilterValidating") { assetFilterValidating = assetFilterRequest.copy() }

                    finishAssetFilterValidation("Asset validation while search is finished")
                }
                chain {
                    title = "Search assets from db"
                    persistenceSearch("Searching asset in db")
                }
                prepareResult("Answer preparation after assets search")
            }
        }.build()
    }
}