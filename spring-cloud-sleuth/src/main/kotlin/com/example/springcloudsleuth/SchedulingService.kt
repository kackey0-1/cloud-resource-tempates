package com.example.springcloudsleuth

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class SchedulingService(
    private val sleuthService: SleuthService
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Scheduled(fixedDelay = 30000)
    @Throws(InterruptedException::class)
    fun scheduledWork() {
        logger.info("Start some work from the scheduled task")
        sleuthService.asyncMethod()
        logger.info("End work from scheduled task")
    }
}
