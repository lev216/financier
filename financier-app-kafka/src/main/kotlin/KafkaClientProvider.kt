package ru.otus.otuskotlin.financier.kafka

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer

class KafkaClientProvider(
    private val config: KafkaConfig,
) {

    fun kafkaConsumer(consumerGroupId: String) =
        KafkaConsumer<String, String>(config.consumerProperties(consumerGroupId))

    fun kafkaProducer() = KafkaProducer<String, String>(config.producerProperties())
}