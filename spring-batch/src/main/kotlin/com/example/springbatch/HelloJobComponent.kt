package com.example.springbatch

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component


@Component
class HelloJobComponent(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {
    @Bean
    fun fooJob(helloStep: Step): Job {
        println("fooJob メソッドを実行")
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
        println("helloStep メソッドを実行")
        return stepBuilderFactory["myHelloStep"]
            .tasklet(MessageTasklet("Hello!"))
            .build()
    }

    @Bean
    fun worldStep(): Step {
        println("worldStep メソッドを実行")
        return stepBuilderFactory["myWorldStep"]
            .tasklet(MessageTasklet("World!"))
            .build()
    }
}
