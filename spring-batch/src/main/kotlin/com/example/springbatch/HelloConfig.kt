package com.example.springbatch

import lombok.RequiredArgsConstructor
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
class HelloConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {
    @Bean
    fun fooJob(): Job {
        println("fooJob メソッドを実行")
        return jobBuilderFactory["myFooJob"]
            .flow(helloStep())
            .end()
            .build()
    }

    @Bean
    fun barJob(): Job {
        println("barJob メソッドを実行")
        return jobBuilderFactory["myBarJob"]
            .flow(helloStep())
            .next(worldStep())
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
