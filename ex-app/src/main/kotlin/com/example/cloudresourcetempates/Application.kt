package com.example.cloudresourcetempates

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.example.cloudresourcetempates"])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
