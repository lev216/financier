package ru.otus.otuskotlin.financier.asset.common.model

@JvmInline
value class AssetId(private val id: String) {

    fun asString() = id

    companion object {
        val NONE = AssetId("")
        val REGEX = Regex("^[0-9a-zA-Z-]+$")
    }
}