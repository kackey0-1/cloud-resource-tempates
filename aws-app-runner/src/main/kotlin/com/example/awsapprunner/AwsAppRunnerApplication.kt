package com.example.awsapprunner

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AwsAppRunnerApplication

fun main(args: Array<String>) {
    runApplication<AwsAppRunnerApplication>(*args)
}
