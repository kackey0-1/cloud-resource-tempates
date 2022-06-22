package org.example.template.stream.model

import lombok.Value

@Value
data class KafkaEventMessage<T>(
    val header: EventHeader,
    val data: T
)
