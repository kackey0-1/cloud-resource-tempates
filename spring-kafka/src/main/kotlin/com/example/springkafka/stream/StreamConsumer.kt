package com.example.springkafka.stream

import com.example.springkafka.config.KafkaProps
import org.example.template.stream.model.KafkaEventMessage
import org.example.template.stream.model.SampleMessageInfo
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Component
class StreamConsumer(
    private val kafkaTemplate: KafkaTemplate<String, KafkaEventMessage<SampleMessageInfo>>,
) {
    private val RETRY: Int = 3
    private val log = LoggerFactory.getLogger(this.javaClass)

    @KafkaListener(topics = [KafkaProps.Topics.SAMPLE_USER_TOPIC])
    fun consume(eventStream: KafkaEventMessage<SampleMessageInfo>) {
        try {
            log.info("received message: {}", eventStream.data.message)
        } catch (ex: RuntimeException) {
            val currentRetryCount = eventStream.data.retryCount
            if (currentRetryCount < RETRY) {
                eventStream.data.incrementRetryCount()
                kafkaTemplate.send(KafkaProps.Topics.SAMPLE_USER_TOPIC, eventStream)
            } else {
                // TODO think about workaround for failure
                throw RuntimeException("process failed for kafka events")
            }
        }
        log.info("completed user trigger event process")
    }
}
