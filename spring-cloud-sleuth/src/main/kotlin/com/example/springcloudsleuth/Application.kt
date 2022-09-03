package com.example.springcloudsleuth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.example.springcloudsleuth"])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
