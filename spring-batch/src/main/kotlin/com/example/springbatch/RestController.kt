package com.example.springbatch

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class RestController(
    private val httpRequestBasedJobLauncher: JobLauncher,
    private val fooJob: Job,
    private val barJob: Job
) {

    @PostMapping("/invokeFooJob")
    fun invokeFooJob(): ResponseEntity<String> {
        println("fooJob メソッドを実行")
        val jobParameters = JobParametersBuilder().addLong("time", System.currentTimeMillis())
            .toJobParameters()
        httpRequestBasedJobLauncher.run(fooJob, jobParameters)
        return ResponseEntity<String>("{\"status\": \"OK\"}", HttpStatus.OK)
    }

    @PostMapping("/invokeBarJob")
    fun invokeBarJob(): ResponseEntity<String> {
        println("barJob メソッドを実行")
        val jobParameters = JobParametersBuilder().addLong("time", System.currentTimeMillis())
            .toJobParameters()
        httpRequestBasedJobLauncher.run(barJob, jobParameters)
        return ResponseEntity<String>("{\"status\": \"OK\"}", HttpStatus.OK)
    }
}
