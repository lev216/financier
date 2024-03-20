package ru.otus.otuskotlin.financier.cor.handler

import ru.otus.otuskotlin.financier.cor.ICorExec
import ru.otus.otuskotlin.financier.cor.ICorExecDsl

abstract class AbstractCorExec<T>(
    override val title: String,
    override val description: String,
    private val blockOn: suspend T.() -> Boolean = { true },
    private val blockException: suspend T.(Throwable) -> Unit = {},
) : ICorExec<T> {
    protected abstract suspend fun handle(context: T)

    protected suspend fun on(context: T): Boolean = context.blockOn()

    protected suspend fun exception(context: T, e: Throwable) = context.blockException(e)

    override suspend fun exec(context: T) {
        if (on(context)) {
            try {
                handle(context)
            } catch (e: Throwable) {
                exception(context, e)
            }
        }
    }
}

abstract class CorExecDsl<T> : ICorExecDsl<T> {
    protected var blockOn: suspend T.() -> Boolean = { true }
    protected var blockException: suspend T.(e: Throwable) -> Unit = { e: Throwable -> throw e }

    override var title: String = ""
    override var description: String = ""

    override fun on(function: suspend T.() -> Boolean) {
        blockOn = function
    }

    override fun exception(function: suspend T.(e: Throwable) -> Unit) {
        blockException = function
    }
}