package ru.otus.otuskotlin.financier.asset.common.persistence

import ru.otus.otuskotlin.financier.asset.common.model.AssetSearchType
import ru.otus.otuskotlin.financier.asset.common.model.UserId
import java.time.LocalDate

data class DbAssetFilterRequest(
    val startDate: LocalDate = LocalDate.EPOCH,
    val endDate: LocalDate = LocalDate.EPOCH.plusYears(1000),
    val userId: UserId = UserId.NONE,
    val type: AssetSearchType = AssetSearchType.NONE,
)
