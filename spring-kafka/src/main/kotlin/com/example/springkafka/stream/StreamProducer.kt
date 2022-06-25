package com.example.springkafka.stream

import com.example.springkafka.config.KafkaProps
import com.example.springkafka.stream.model.SampleMessageInfo
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@Component
class StreamProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Async("asyncExecutor")
    fun produce(eventMessage: SampleMessageInfo) {
        log.info(
            "send message for: {} at {} trial",
            eventMessage.message,
            eventMessage.retryCount
        )
        send(SampleMessageInfo::class.java, KafkaProps.Topics.SAMPLE_USER_TOPIC, eventMessage)
    }

    private fun <V> send(clazz: Class<V>, topic: String, message: V) {
        try {
            (castedKafkaTemplate(clazz)).send(topic, message)
                .get(10, TimeUnit.SECONDS)
        } catch (e: ExecutionException) {
            log.error(e.message, e)
            throw ExecutionException(e)
        } catch (e: TimeoutException) {
            log.error(e.message, e)
            throw TimeoutException(e.message)
        } catch (e: InterruptedException) {
            log.error(e.message, e)
            throw InterruptedException(e.message)
        } catch (e: Exception) {
            log.error(e.message, e)
            throw Exception(e)
        }
    }

    private fun <V> castedKafkaTemplate(clazz: Class<V>): KafkaTemplate<String, V> {
        return kafkaTemplate as KafkaTemplate<String, V>
    }
}
