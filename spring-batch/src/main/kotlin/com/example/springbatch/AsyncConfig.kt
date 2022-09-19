package com.example.springbatch

import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.SimpleJobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.annotation.AsyncConfigurer

@Configuration
class AsyncConfig(
    private val jobRepository: JobRepository
) : AsyncConfigurer {

    @Bean
    fun asyncJobLauncher(asyncTaskExecutor: TaskExecutor): JobLauncher {
        val jobLauncher = SimpleJobLauncher()
        jobLauncher.setJobRepository(jobRepository)
        jobLauncher.setTaskExecutor(asyncTaskExecutor)
        jobLauncher.afterPropertiesSet()
        return jobLauncher
    }

    @Bean
    fun asyncTaskExecutor(): TaskExecutor {
        val asyncTaskExecutor = SimpleAsyncTaskExecutor()
        asyncTaskExecutor.concurrencyLimit = 10
        return asyncTaskExecutor
    }
}
