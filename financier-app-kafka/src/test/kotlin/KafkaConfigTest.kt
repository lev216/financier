package ru.otus.otuskotlin.financier.kafka

import org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG
import org.apache.kafka.clients.CommonClientConfigs.GROUP_ID_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class KafkaConfigTest {
    private companion object {
        const val SERVER = "http://localhost:80"
        const val CONSUMER_GROUP = "consumerGroup"
    }

    private val config = KafkaConfig(SERVER)

    @Test
    fun consumerProperties() {
        val actual = config.consumerProperties(CONSUMER_GROUP)
        assertThat(actual[BOOTSTRAP_SERVERS_CONFIG]).isEqualTo(SERVER)
        assertThat(actual[GROUP_ID_CONFIG]).isEqualTo(CONSUMER_GROUP)
        assertThat(actual[KEY_DESERIALIZER_CLASS_CONFIG]).isEqualTo(StringDeserializer::class.java)
        assertThat(actual[VALUE_DESERIALIZER_CLASS_CONFIG]).isEqualTo(StringDeserializer::class.java)
    }

    @Test
    fun producerProperties() {
        val actual = config.producerProperties()
        assertThat(actual[BOOTSTRAP_SERVERS_CONFIG]).isEqualTo(SERVER)
        assertThat(actual[KEY_SERIALIZER_CLASS_CONFIG]).isEqualTo(StringSerializer::class.java)
        assertThat(actual[VALUE_SERIALIZER_CLASS_CONFIG]).isEqualTo(StringSerializer::class.java)
    }
}