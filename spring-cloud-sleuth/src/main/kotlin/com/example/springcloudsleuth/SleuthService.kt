package com.example.springcloudsleuth

import brave.Tracer
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class SleuthService(
    private val tracer: Tracer
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Throws(InterruptedException::class)
    fun doSomeWorkSameSpan() {
        Thread.sleep(1000L)
        logger.info("Doing some work")
    }

    @Throws(InterruptedException::class)
    fun doSomeWorkNewSpan() {
        logger.info("I'm in the original span")
        val newSpan = tracer.nextSpan().name("newSpan").start()
        try {
            tracer.withSpanInScope(newSpan.start()).use {
                Thread.sleep(1000L)
                logger.info("I'm in the new span doing some cool work that needs its own span")
            }
        } finally {
            newSpan.finish()
        }
        logger.info("I'm in the original span")
    }

    @Async
    @Throws(InterruptedException::class)
    fun asyncMethod() {
        logger.info("Start Async Method")
        Thread.sleep(1000L)
        logger.info("End Async Method")
    }
}
