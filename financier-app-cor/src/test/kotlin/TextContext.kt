package ru.otus.otuskotlin.financier.cor

data class TextContext(
    var status: CorStatus = CorStatus.NONE,
    var some: Int = 0,
    var history: String = "",
) {

    enum class CorStatus {
        NONE,
        RUNNING,
        FAILING,
        ERROR,
    }
}