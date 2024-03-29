package ru.otus.otuskotlin.financier.asset.common.model

@JvmInline
value class AssetRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = AssetRequestId("")
    }
}