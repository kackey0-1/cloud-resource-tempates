package com.example.springkafka

import com.example.springkafka.kafka.Producer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class KafkaController(
    private val producer: Producer
) {

    @PostMapping("/v1/event")
    fun triggerUserChange(@RequestBody message: String): ResponseEntity<Response> {
        producer.sendMessage(message)
        return ResponseEntity(Response(), HttpStatus.OK)
    }

    data class Response(
        val status: String = "UP"
    )
}
