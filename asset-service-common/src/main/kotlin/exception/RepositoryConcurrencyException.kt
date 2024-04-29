package ru.otus.otuskotlin.financier.asset.common.exception

import ru.otus.otuskotlin.financier.asset.common.model.AssetLock
import java.lang.RuntimeException

class RepositoryConcurrencyException(expectedLock: AssetLock, actualLock: AssetLock?) : RuntimeException (
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)