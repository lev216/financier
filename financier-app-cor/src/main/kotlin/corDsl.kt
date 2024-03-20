package ru.otus.otuskotlin.financier.cor

import ru.otus.otuskotlin.financier.cor.handler.CorChainDsl
import ru.otus.otuskotlin.financier.cor.handler.CorWorkerDsl
import ru.otus.otuskotlin.financier.cor.handler.executeParallel

@CorDslMarker
interface ICorExecDsl<T> {
    var title: String
    var description: String

    fun on(function: suspend T.() -> Boolean)

    fun exception(function: suspend T.(e: Throwable) -> Unit)

    fun build(): ICorExec<T>
}

@CorDslMarker
interface ICorChainDsl<T> : ICorExecDsl<T> {
    fun add(worker: ICorExecDsl<T>)
}

@CorDslMarker
interface ICorWorkerDsl<T> : ICorExecDsl<T> {
    fun handle(function: suspend T.() -> Unit)
}

fun <T> rootChain(function: ICorChainDsl<T>.() -> Unit): ICorChainDsl<T> = CorChainDsl<T>().apply(function)

fun <T> ICorChainDsl<T>.chain(function: ICorChainDsl<T>.() -> Unit) {
    add(CorChainDsl<T>().apply(function))
}

fun <T> ICorChainDsl<T>.parallel(function: ICorChainDsl<T>.() -> Unit) {
    add(CorChainDsl<T>(::executeParallel).apply(function))
}

fun <T> ICorChainDsl<T>.worker(function: ICorWorkerDsl<T>.() -> Unit) {
    add(CorWorkerDsl<T>().apply(function))
}

fun <T> ICorChainDsl<T>.worker(
    title: String,
    description: String = "",
    blockHandle: T.() -> Unit,
) {
    add(CorWorkerDsl<T>().also {
        it.title = title
        it.description = description
        it.handle(blockHandle)
    })
}

