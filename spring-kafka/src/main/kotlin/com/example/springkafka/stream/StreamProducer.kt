package com.example.springkafka.stream

import com.example.springkafka.config.KafkaProps
import org.example.template.stream.model.KafkaEventMessage
import org.example.template.stream.model.SampleMessageInfo
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.util.concurrent.ListenableFuture

@Component
class StreamProducer(
    private val kafkaTemplate: KafkaTemplate<*, *>,
) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Async("asyncExecutor")
    fun produce(eventMessage: KafkaEventMessage<SampleMessageInfo>): ListenableFuture<out SendResult<out Any, out Any>> {
        log.info(
            "send message for: {} at {} trial",
            eventMessage.data.message,
            eventMessage.data.retryCount
        )
        return (castedKafkaTemplate(KafkaEventMessage::class.java))
            .send(KafkaProps.Topics.SAMPLE_USER_TOPIC, eventMessage)
    }

    private fun <V> castedKafkaTemplate(clazz: Class<V>): KafkaTemplate<String, V> {
        return kafkaTemplate as KafkaTemplate<String, V>
    }
}
