package com.example.springbatch.batch.hello

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.listener.JobExecutionListenerSupport
import org.springframework.stereotype.Component

@Component
class HelloJobListenerAOP : JobExecutionListenerSupport() {
    private val log = LoggerFactory.getLogger(HelloJobListenerAOP::class.java)

    override fun beforeJob(jobExecution: JobExecution) {
        log.info(jobExecution.toString())
    }

    override fun afterJob(jobExecution: JobExecution) {
        log.info(jobExecution.toString())
    }
}
