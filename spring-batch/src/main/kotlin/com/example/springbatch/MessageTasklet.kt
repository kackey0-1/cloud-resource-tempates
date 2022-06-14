package com.example.springbatch

import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus


class MessageTasklet(
    var message: String = ""
) : Tasklet {

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        println("Message: $message")
        Thread.sleep(1000)
        return RepeatStatus.FINISHED
    }
}
