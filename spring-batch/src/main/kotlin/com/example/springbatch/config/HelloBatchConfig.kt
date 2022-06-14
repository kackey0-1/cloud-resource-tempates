package com.example.springbatch.config

import com.example.springbatch.batchimpl.MessageTasklet
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor

@Configuration
class HelloBatchConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val asyncTaskExecutor: TaskExecutor
) {
    @Bean
    fun fooJob(helloStep: Step): Job {
        return jobBuilderFactory["myFooJob"]
            .flow(helloStep)
            .end()
            .build()
    }

    @Bean
    fun barJob(helloStep: Step, worldStep: Step): Job {
        return jobBuilderFactory["myBarJob"]
            .flow(helloStep)
            .next(worldStep)
            .end()
            .build()
    }

    @Bean
    fun helloStep(): Step {
        return stepBuilderFactory["myHelloStep"]
            .tasklet(MessageTasklet("Hello!"))
            .taskExecutor(asyncTaskExecutor)
            .build()
    }

    @Bean
    fun worldStep(): Step {
        return stepBuilderFactory["myWorldStep"]
            .tasklet(MessageTasklet("World!"))
            .taskExecutor(asyncTaskExecutor)
            .build()
    }
}
