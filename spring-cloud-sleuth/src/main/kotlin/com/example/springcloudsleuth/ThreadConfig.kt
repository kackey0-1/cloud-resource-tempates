package com.example.springcloudsleuth

import org.springframework.beans.factory.BeanFactory
import org.springframework.cloud.sleuth.instrument.async.LazyTraceExecutor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurerSupport
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Configuration
@EnableAsync
@EnableScheduling
class ThreadConfig(
    private val beanFactory: BeanFactory
) : AsyncConfigurerSupport(), SchedulingConfigurer {

    @Bean
    fun executor(): Executor {
        val threadPoolTaskExecutor = ThreadPoolTaskExecutor()
        threadPoolTaskExecutor.corePoolSize = 1
        threadPoolTaskExecutor.maxPoolSize = 1
        threadPoolTaskExecutor.initialize()
        return LazyTraceExecutor(beanFactory, threadPoolTaskExecutor)
    }

    override fun getAsyncExecutor(): Executor {
        val threadPoolTaskExecutor = ThreadPoolTaskExecutor()
        threadPoolTaskExecutor.corePoolSize = 1
        threadPoolTaskExecutor.maxPoolSize = 1
        threadPoolTaskExecutor.initialize()
        return LazyTraceExecutor(beanFactory, threadPoolTaskExecutor)
    }

    override fun configureTasks(scheduledTaskRegistrar: ScheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(schedulingExecutor())
    }

    @Bean(destroyMethod = "shutdown")
    fun schedulingExecutor(): Executor {
        return Executors.newScheduledThreadPool(1)
    }
}
