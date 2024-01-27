package ru.otus.otuskotlin.financier.asset.common.model

import ru.otus.otuskotlin.financier.asset.common.model.AssetSearchType.NONE
import java.time.LocalDate

data class AssetFilter(
    val id: AssetId = AssetId.NONE,
    val startDate: LocalDate = LocalDate.MIN,
    val endDate: LocalDate = LocalDate.MAX,
    val userId: UserId = UserId.NONE,
    val type: AssetSearchType = NONE,
)

enum class AssetSearchType {
    CASH,
    DEPOSIT,
    NONE,
}