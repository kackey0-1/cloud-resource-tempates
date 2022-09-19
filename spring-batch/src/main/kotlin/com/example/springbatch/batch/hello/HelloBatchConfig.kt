package com.example.springbatch.batch.hello

import com.example.springbatch.batch.impl.MessageTasklet
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HelloBatchConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
) {
    @Bean
    fun fooJob(helloStep: Step): Job {
        return jobBuilderFactory["myFooJob"]
            .flow(helloStep)
            .end()
            .build()
    }

    @Bean
    fun barJob(
        helloStep: Step,
        worldStep: Step,
        listener: HelloJobListenerAOP
    ): Job {
        return jobBuilderFactory["myBarJob"]
            .incrementer(RunIdIncrementer())
            .listener(listener)
            .flow(helloStep)
            .next(worldStep)
            .end()
            .build()
    }

    @Bean
    fun helloStep(): Step {
        return stepBuilderFactory["myHelloStep"]
            .tasklet(MessageTasklet("Hello!"))
            .build()
    }

    @Bean
    fun worldStep(): Step {
        return stepBuilderFactory["myWorldStep"]
            .tasklet(MessageTasklet("World!"))
            .build()
    }
}
