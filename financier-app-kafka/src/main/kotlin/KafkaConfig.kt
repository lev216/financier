package ru.otus.otuskotlin.financier.kafka

import org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG
import org.apache.kafka.clients.CommonClientConfigs.GROUP_ID_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import java.util.Properties

class KafkaConfig(
    private val server: String,
) {

    fun consumerProperties(consumerGroupId: String) = Properties().apply {
        set(BOOTSTRAP_SERVERS_CONFIG, server)
        set(GROUP_ID_CONFIG, consumerGroupId)
        set(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
        set(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
    }

    fun producerProperties() = Properties().apply {
        set(BOOTSTRAP_SERVERS_CONFIG, server)
        set(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
        set(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
    }

}