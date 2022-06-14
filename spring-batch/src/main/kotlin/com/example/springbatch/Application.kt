package com.example.springbatch

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@EnableBatchProcessing
@SpringBootApplication(scanBasePackages = ["com.example.springbatch"])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
