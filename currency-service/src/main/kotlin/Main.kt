package ru.otus.otuskotlin.financier.currency

import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("Main")
    while (true) {
        logger.info("This is currency-service!")
        Thread.sleep(1000)
    }
}