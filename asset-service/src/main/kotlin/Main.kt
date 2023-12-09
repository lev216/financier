package ru.otus.otuskotlin.financier.asset

import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("Main")
    while (true) {
        logger.info("This is asset-service!")
        Thread.sleep(1000)
    }
}