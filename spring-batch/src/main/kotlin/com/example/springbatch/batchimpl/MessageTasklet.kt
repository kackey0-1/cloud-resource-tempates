package com.example.springbatch.batchimpl

import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus

class MessageTasklet(
    var message: String = ""
) : Tasklet {

    private val log = LoggerFactory.getLogger(MessageTasklet::class.java)

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        log.info("Message: $message")
        // println("Execution time: ${Instant.ofEpochMilli(time)}")
        Thread.sleep(1000)
        return RepeatStatus.FINISHED
    }
}
