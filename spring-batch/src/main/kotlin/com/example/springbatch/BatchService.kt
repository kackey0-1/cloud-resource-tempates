package com.example.springbatch

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.JobParametersInvalidException
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import javax.batch.operations.JobExecutionAlreadyCompleteException
import javax.batch.operations.JobRestartException

@Service
class BatchService(
    private val jobLauncher: JobLauncher,
) {


}
