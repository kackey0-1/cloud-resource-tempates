package com.example.springbatch

import lombok.RequiredArgsConstructor
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus


@RequiredArgsConstructor // Lombok によるコンストラクタ自動生成
class MessageTasklet(
    var message: String = ""
) : Tasklet {

    @Throws(Exception::class)
    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        println("Message: $message")
        return RepeatStatus.FINISHED
    }
}
