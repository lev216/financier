package ru.otus.otuskotlin.financier.asset

object Env {
    fun getVar(key: String, default: String): String = System.getenv(key) ?: default
}