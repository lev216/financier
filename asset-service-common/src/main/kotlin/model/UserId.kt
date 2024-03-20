package ru.otus.otuskotlin.financier.asset.common.model

@JvmInline
value class UserId(private val id: String) {

    fun asString() = id

    companion object {
        val NONE = UserId("")
        val REGEX = Regex("^[0-9a-zA-Z-]+$")
    }
}