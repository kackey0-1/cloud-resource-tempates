package com.example.springkafka

import com.example.springkafka.stream.StreamProducer
import org.example.template.stream.model.EventHeader
import org.example.template.stream.model.KafkaEventMessage
import org.example.template.stream.model.SampleMessageInfo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime

@RestController
class KafkaController(
    private val streamProducer: StreamProducer
) {
    private val VERSION_1_0 = "1.0"

    @PostMapping("/v1/stream")
    fun triggerStream(@RequestBody message: String): ResponseEntity<Response> {
        val eventHeader = EventHeader(VERSION_1_0, ZonedDateTime.now().toInstant().toEpochMilli())
        val eventMessage = KafkaEventMessage<SampleMessageInfo>(eventHeader, SampleMessageInfo(message))
        streamProducer.produce(eventMessage)
        return ResponseEntity(Response(), HttpStatus.OK)
    }

    data class Response(
        val status: String = "UP"
    )
}
