package com.example.springbatch

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.JobParametersInvalidException
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import javax.batch.operations.JobExecutionAlreadyCompleteException
import javax.batch.operations.JobRestartException

@RestController
class JobController(
    private val asyncJobLauncher: JobLauncher,
    private val fooJob: Job,
    private val barJob: Job,
    private val importUserJob: Job,
    private val customerJob: Job
) {

    @PostMapping("/invokeFooJob")
    fun invokeFooJob(): ResponseEntity<String> {
        val jobParameters = JobParametersBuilder().toJobParameters()
        val result = asyncJobLauncher.run(fooJob, jobParameters)
        return ResponseEntity<String>(result.toString(), HttpStatus.OK)
    }

    @PostMapping("/invokeBarJob")
    fun invokeBarJob(): ResponseEntity<String> {
        // TODO: add retry count param
        val jobParameters = JobParametersBuilder().toJobParameters()
        val result = asyncJobLauncher.run(barJob, jobParameters)
        return ResponseEntity<String>(result.toString(), HttpStatus.OK)
    }

    @PostMapping("/invokeImportUserJob")
    fun invokeImportUserJob(): ResponseEntity<String> {
        val jobParameters = JobParametersBuilder().addLong("time", System.currentTimeMillis())
            .toJobParameters()
        asyncJobLauncher.run(importUserJob, jobParameters)
        return ResponseEntity<String>("{\"status\": \"OK\"}", HttpStatus.OK)
    }

    @PostMapping("/invokeImportCustomers")
    fun invokeImportCustomers() {
        val jobParameters = JobParametersBuilder()
            .addLong("startAt", System.currentTimeMillis()).toJobParameters()

        try {
            asyncJobLauncher.run(customerJob, jobParameters)
        } catch (e: JobExecutionAlreadyCompleteException) {
            e.printStackTrace()
        } catch (e: JobRestartException) {
            e.printStackTrace()
        } catch (e: JobInstanceAlreadyCompleteException) {
            e.printStackTrace()
        } catch (e: JobParametersInvalidException) {
            e.printStackTrace()
        }
    }
}
