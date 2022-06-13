package com.example.springbatch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication(scanBasePackages = ["com.example.springbatch"])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
