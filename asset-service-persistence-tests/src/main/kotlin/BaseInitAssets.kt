package ru.otus.otuskotlin.financier.asset.persistence.tests

import ru.otus.otuskotlin.financier.asset.common.model.*

abstract class BaseInitAssets(val op: String) : IInitObjects<Asset> {
    open val lockOld: AssetLock = AssetLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: AssetLock = AssetLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        userId: UserId = UserId("user-123"),
        lock: AssetLock = lockOld,
    ) = Cash(
        id = AssetId("cash-repo-$op-$suf"),
        userId = userId,
        lock = lock,
    )
}