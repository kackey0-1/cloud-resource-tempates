package com.example.springkafka.kafka

import com.example.springkafka.kafka.config.KafkaConfig
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class Consumer {

    @KafkaListener(topics = [KafkaConfig.KAFKA_TOPIC_NAME], groupId = KafkaConfig.KAFKA_GROUP_ID)
    fun listenGroupFoo(message: String) {
        println("Received Message in group foo: $message")
    }
}
