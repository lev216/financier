package ru.otus.otuskotlin.financier.cor.handler

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.Test
import ru.otus.otuskotlin.financier.cor.TextContext

class CorChainTest {

    @Test
    fun `chain should execute workers`() = runTest {
        val createWorker = { title: String ->
            CorWorker<TextContext>(
                title = title,
                blockOn = { status == TextContext.CorStatus.NONE },
                blockHandle = { history += "$title; "},
            )
        }
        val chain = CorChain(
            execs = listOf(createWorker("w1"), createWorker("w2")),
            title = "chain",
            handler = ::executeSequential
        )
        val context = TextContext()
        chain.exec(context)
        assertThat(context.history).isEqualTo("w1; w2; ")
    }

    @Test
    fun `chain should not execute workers`() = runTest {
        val createWorker = { title: String ->
            CorWorker<TextContext>(
                title = title,
                blockOn = { status == TextContext.CorStatus.NONE },
                blockHandle = { history += "$title; "},
            )
        }
        val chain = CorChain(
            execs = listOf(createWorker("w1"), createWorker("w2")),
            title = "chain",
            handler = ::executeSequential
        )
        val context = TextContext(status = TextContext.CorStatus.RUNNING)
        chain.exec(context)
        assertThat(context.history).isEmpty()
    }
}