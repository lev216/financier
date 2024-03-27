package ru.otus.otuskotlin.financier.cor

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.assertj.core.api.AssertionsForClassTypes.assertThatCode
import org.junit.Test

class CorDslTest {

    private suspend fun execute(dsl: ICorExecDsl<TextContext>): TextContext {
        val context = TextContext()
        dsl.build().exec(context)
        return context
    }

    @Test
    fun `handle should execute`() = runTest {
        val chain = rootChain<TextContext> {
            worker {
                handle { history += "w1; " }
            }
        }
        val context = execute(chain)
        assertThat(context.history).isEqualTo("w1; ")
    }

    @Test
    fun `on should check condition`() = runTest {
        val chain = rootChain<TextContext> {
            worker {
                on { status == TextContext.CorStatus.ERROR }
                handle { history += "w1; " }
            }
            worker {
                on { status == TextContext.CorStatus.NONE }
                handle {
                    history += "w2; "
                    status = TextContext.CorStatus.FAILING
                }
            }
            worker {
                on { status == TextContext.CorStatus.FAILING}
                handle { history += "w3; " }
            }
        }
        assertThat(execute(chain).history).isEqualTo("w2; w3; ")
    }

    @Test
    fun `exception should execute`() = runTest {
        val chain = rootChain<TextContext> {
            worker {
                handle { throw RuntimeException("some error") }
                exception { history += it.message }
            }
        }
        assertThat(execute(chain).history).isEqualTo("some error")
    }

    @Test
    fun `exception should be thrown if exception block is absent`() = runTest {
        val chain = rootChain<TextContext> {
            worker("throw") { throw RuntimeException("some error") }
        }
        assertThatCode { runBlocking { execute(chain) } }.isInstanceOf(RuntimeException::class.java)
    }

    @Test
    fun `complex test`() = runTest {
        val chain = rootChain<TextContext> {
            worker {
                title = "Status initialization"
                description = "Check initial status and set the new one"

                on { status == TextContext.CorStatus.NONE }
                handle { status = TextContext.CorStatus.RUNNING }
                exception { status = TextContext.CorStatus.ERROR }
            }
            chain {
                on { status == TextContext.CorStatus.RUNNING }
                worker(
                    title = "Add value",
                    description = "Add some int value",
                ) {
                    some += 4
                }
            }
            parallel {
                on {
                    some < 5
                }
                worker(title = "Increment some") {
                    some++
                }
            }
            printResult()
        }.build()

        val context = TextContext()
        chain.exec(context)
        assertThat(context.some).isEqualTo(5)
    }

    private fun ICorChainDsl<TextContext>.printResult() = worker(title = "Print result") {
        println("Some: $some")
    }
}