package ru.otus.otuskotlin.financier.cor.handler

import ru.otus.otuskotlin.financier.cor.CorDslMarker
import ru.otus.otuskotlin.financier.cor.ICorExec
import ru.otus.otuskotlin.financier.cor.ICorWorkerDsl

class CorWorker<T>(
    title: String,
    description: String = "",
    blockOn: suspend T.() -> Boolean = { true },
    blockException: suspend T.(Throwable) -> Unit = {},
    private val blockHandle: suspend T.() -> Unit = {},
) : AbstractCorExec<T>(title, description, blockOn, blockException) {
    override suspend fun handle(context: T) = blockHandle(context)
}

@CorDslMarker
class CorWorkerDsl<T> : CorExecDsl<T>(), ICorWorkerDsl<T> {
    private var blockHandle: suspend T.() -> Unit = {}

    override fun handle(function: suspend T.() -> Unit) {
        blockHandle = function
    }

    override fun build(): ICorExec<T> = CorWorker(
        title = title,
        description = description,
        blockOn = blockOn,
        blockException = blockException,
        blockHandle = blockHandle,
    )
}