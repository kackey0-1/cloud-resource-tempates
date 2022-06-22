package com.example.springkafka.config

import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@Configuration
@EnableAsync
@RequiredArgsConstructor
class AsyncConfig : AsyncConfigurer {

    private val executorShutdownGracePeriodInSeconds = 0

    @Bean("asyncExecutor")
    override fun getAsyncExecutor(): Executor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 10
        executor.setThreadNamePrefix("AsyncExecutor-")
        executor.setAwaitTerminationMillis(executorShutdownGracePeriodInSeconds.toLong())
        executor.initialize()
        return executor
    }
}
