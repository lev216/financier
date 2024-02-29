package ru.otus.otuskotlin.financier.kafka

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

import org.junit.Assert.*
import java.util.*

class KafkaClientProviderTest {
    private val config = mockk<KafkaConfig>()

    private val provider = KafkaClientProvider(config)

    @Test
    fun kafkaConsumer() {
        val properties = Properties().also {
            it[CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG] = "http://localhost:80"
            it[CommonClientConfigs.GROUP_ID_CONFIG] = "group"
            it[KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
            it[VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        }
        every { config.consumerProperties(any()) } returns properties

        val actual = provider.kafkaConsumer("group")

        assertThat(actual).isNotNull
        assertThat(actual).isInstanceOf(KafkaConsumer::class.java)
        assertThat(actual.groupMetadata().groupId()).isEqualTo("group")
        verify { config.consumerProperties("group") }
    }

    @Test
    fun kafkaProducer() {
        val properties = Properties().also {
            it[CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG] = "http://localhost:80"
            it[KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
            it[VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        }
        every { config.producerProperties() } returns properties

        val actual = provider.kafkaProducer()
        assertThat(actual).isNotNull
        assertThat(actual).isInstanceOf(KafkaProducer::class.java)
        verify { config.producerProperties() }
    }
}