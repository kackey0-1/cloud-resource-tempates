package com.example.springcloudsleuth

import brave.Tracer
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SleuthTraceIdController(
    private val tracer: Tracer
) {
    companion object {
        private val logger = LoggerFactory.getLogger(SleuthTraceIdController::class.java)
    }

    @GetMapping("/traceid")
    fun sleuthTraceId(): String {
        logger.info("Hello with Sleuth")
        val span = tracer.currentSpan()
        if (span != null) {
            logger.info("Span ID hex {}", span.context().spanIdString())
            logger.info("Span ID decimal {}", span.context().spanId())
            logger.info("Trace ID hex {}", span.context().traceIdString())
            logger.info("Trace ID decimal {}", span.context().traceId())
        }
        return "Hello from Sleuth"
    }
}
