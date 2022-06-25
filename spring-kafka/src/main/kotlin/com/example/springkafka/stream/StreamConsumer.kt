package com.example.springkafka.stream

import com.example.springkafka.config.KafkaProps
import com.example.springkafka.stream.model.SampleMessageInfo
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class StreamConsumer(
    private val producer: StreamProducer,
) {
    private val RETRY: Int = 3
    private val log = LoggerFactory.getLogger(this.javaClass)

    @KafkaListener(topics = [KafkaProps.Topics.SAMPLE_USER_TOPIC])
    fun consume(eventStream: SampleMessageInfo) {
        try {
            log.info("received message: {}", eventStream.message)
        } catch (ex: RuntimeException) {
            val currentRetryCount = eventStream.retryCount
            if (currentRetryCount < RETRY) {
                eventStream.incrementRetryCount()
                producer.produce(eventStream)
            } else {
                // TODO think about workaround for failure
                throw RuntimeException("process failed for kafka events")
            }
        }
        log.info("completed sample.topic.trigger event process")
    }
}
