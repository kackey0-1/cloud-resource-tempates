package com.example.springkafka.kafka

import com.example.springkafka.kafka.config.KafkaConfig
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service


@Service
class Producer(
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    @Async("asyncExecutor")
    fun sendMessage(msg: String) {
        Thread.sleep(1000)
        kafkaTemplate.send(KafkaConfig.KAFKA_TOPIC_NAME, msg)
    }
}
