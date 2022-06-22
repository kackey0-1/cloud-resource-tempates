package org.example.template.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer

@EnableKafka
@Configuration
class KafkaConfig(
    private val kafkaProperties: KafkaProperties
) {

    @Bean
    fun objectMapper(): ObjectMapper {
        return Jackson2ObjectMapperBuilder()
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .indentOutput(false)
            .build()
    }

    @Bean
    fun producerFactory(objectMapper: ObjectMapper): ProducerFactory<*, *> {
        val properties = kafkaProperties.buildProducerProperties()
        val serializer: JsonSerializer<Object> = JsonSerializer(objectMapper)
        return DefaultKafkaProducerFactory<String, Object>(properties, StringSerializer(), serializer)
    }

    @Bean
    fun consumerFactory(objectMapper: ObjectMapper): ConsumerFactory<*, *> {
        val properties = kafkaProperties.buildConsumerProperties()
        val deserializer: JsonDeserializer<Object> = JsonDeserializer(Object::class.java, objectMapper)
        return DefaultKafkaConsumerFactory<String, Object>(properties, StringDeserializer(), deserializer)
    }
}
