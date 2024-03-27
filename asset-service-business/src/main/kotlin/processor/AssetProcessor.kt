package ru.otus.otuskotlin.financier.asset.business.processor

import ru.otus.otuskotlin.financier.asset.business.exception.ServiceException
import ru.otus.otuskotlin.financier.asset.business.group.operation
import ru.otus.otuskotlin.financier.asset.business.group.stubs
import ru.otus.otuskotlin.financier.asset.business.validation.*
import ru.otus.otuskotlin.financier.asset.business.worker.*
import ru.otus.otuskotlin.financier.asset.common.AssetContext
import ru.otus.otuskotlin.financier.asset.common.model.*
import ru.otus.otuskotlin.financier.asset.common.model.AssetCommand.*
import ru.otus.otuskotlin.financier.cor.rootChain
import ru.otus.otuskotlin.financier.cor.worker

class AssetProcessor {
    suspend fun exec(context: AssetContext) = BusinessChain.exec(context)

    companion object {
        private val BusinessChain = rootChain {
            initStatus("Status initialization")

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
            }
        }.build()

        private fun Asset.copy() = when(this) {
            is Cash -> this.copy()
            is Deposit -> this.copy()
            else -> throw ServiceException("Type ${this::class.java} is unknown")
        }
    }
}