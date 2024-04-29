package ru.otus.otuskotlin.financier.asset.persistence.tests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes

fun runPersistenceTest(testBody: suspend TestScope.() -> Unit) = runTest(timeout = 2.minutes) {
    withContext(Dispatchers.Default) {
        testBody()
    }
}