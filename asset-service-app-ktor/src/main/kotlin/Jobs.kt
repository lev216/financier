package ru.otus.otuskotlin.financier.asset

import org.slf4j.LoggerFactory
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit.SECONDS

class Jobs(
    private val jobs: List<Runnable>,
) {
    private val executor = Executors.newFixedThreadPool(jobs.size)
    private val logger = LoggerFactory.getLogger(Jobs::class.java)

    fun start() {
        logger.info("Starting jobs executor")
        jobs.forEach {
            executor.submit(it)
            logger.info("${it::class.java} is started")
        }
    }

    fun stop() {
        logger.info("Stopping jobs executor")
        executor.shutdown()
        executor.awaitTermination(60, SECONDS)
    }
}