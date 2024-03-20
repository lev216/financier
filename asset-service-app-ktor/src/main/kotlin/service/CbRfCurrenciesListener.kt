package ru.otus.otuskotlin.financier.asset.service

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.*
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.common.errors.WakeupException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.financier.asset.Config
import ru.otus.otuskotlin.financier.asset.Env
import ru.otus.otuskotlin.financier.kafka.KafkaClientProvider
import ru.otus.otuskotlin.financier.kafka.KafkaConfig
import java.time.Duration

class CbRfCurrenciesListener(
    kafkaConfig: KafkaConfig = KafkaConfig(Env.getVar("KAFKA_SERVER_URL", "http://localhost:9092")),
    private val config: Config,
    private val consumer: Consumer<String, String> = KafkaClientProvider(config = kafkaConfig).kafkaConsumer(config.getProperty("asset.service.consumer.group")),
    private val logger: Logger = LoggerFactory.getLogger(CbRfCurrenciesListener::class.java)
) : Thread() {

    private val process = atomic(true)

    override fun run() = runBlocking {
        try {
            val topics = listOf(config.getProperty("cb.rf.currency.topic"))
            consumer.subscribe(topics)
            while (process.value) {
                val records: ConsumerRecords<String, String> = withContext(Dispatchers.IO) {
                    consumer.poll(Duration.ofSeconds(1))
                }
                records.forEach { record: ConsumerRecord<String, String> ->
                    logger.info("Record is: ${record.value()}")
                    //TODO: save in cache e.g. Redis
                }
            }
        } catch (e: WakeupException) {
            logger.error("Exception occurred while sending currencies", e)
        } catch (e: RuntimeException) {
            withContext(NonCancellable) {
                throw e
            }
        } finally {
            withContext(NonCancellable) {
                consumer.close()
            }
        }
    }

    fun stopConsuming() {
        logger.warn("CbRfCurrenciesListener stops consuming")
        process.value = false
    }
}