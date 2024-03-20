package ru.otus.otuskotlin.financier.asset.service

import io.mockk.*
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.common.TopicPartition
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.Test
import org.slf4j.Logger
import ru.otus.otuskotlin.financier.asset.Config
import ru.otus.otuskotlin.financier.kafka.KafkaConfig

class CbRfCurrenciesListenerTest {
    private val kafkaConfig = mockk<KafkaConfig>()
    private val config = mockk<Config>()
    private val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
    private val logger = mockk<Logger>()

    private val service = CbRfCurrenciesListener(kafkaConfig, config, consumer, logger)

    @Test
    fun sendCurrencies() {
        every { config.getProperty(any()) } returns "topic"
        consumer.schedulePollTask {
            consumer.rebalance(listOf(TopicPartition("topic", 0)))
            consumer.addRecord(
                ConsumerRecord(
                    "topic",
                    0,
                    0L,
                    "key",
                    "{\"currencies\":{\"AUD\":59.0059}}",
                )
            )
            service.stopConsuming()
        }
        val startOffsets = mutableMapOf<TopicPartition, Long>()
        val tp = TopicPartition("topic", 0)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)
        every { logger.info(any()) } returns Unit
        every { logger.warn(any()) } returns Unit

        service.run()

        val slot = slot<String>()
        verify { config.getProperty("cb.rf.currency.topic") }
        verify { logger.info(capture(slot)) }
        val actualValue = slot.captured
        assertThat(actualValue).isEqualTo("Record is: {\"currencies\":{\"AUD\":59.0059}}")
        verify { logger.warn("CbRfCurrenciesListener stops consuming") }
    }
}