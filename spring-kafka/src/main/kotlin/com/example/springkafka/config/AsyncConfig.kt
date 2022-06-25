package com.example.springkafka.config

import com.example.springkafka.handler.AsyncExceptionHandler
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@Configuration
@EnableAsync
class AsyncConfig(
    private val asyncExceptionHandler: AsyncExceptionHandler
) : AsyncConfigurer {

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

    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler {
        return asyncExceptionHandler
    }
}
