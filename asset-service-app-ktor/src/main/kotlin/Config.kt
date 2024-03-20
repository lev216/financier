package ru.otus.otuskotlin.financier.asset

import java.util.Properties

object Config {
    private val properties = Properties()

    init {
        val file = this::class.java.classLoader.getResourceAsStream("config.properties")
        properties.load(file)
    }

    fun getProperty(key: String) = properties.getProperty(key)
        ?: throw IllegalStateException("Property $key is not initialized")

}