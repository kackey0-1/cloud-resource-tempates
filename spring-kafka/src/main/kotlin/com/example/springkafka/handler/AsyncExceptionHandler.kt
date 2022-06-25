package com.example.springkafka.handler

import org.slf4j.LoggerFactory
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Component
class AsyncExceptionHandler : AsyncUncaughtExceptionHandler {

    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun handleUncaughtException(ex: Throwable, method: Method, vararg params: Any) {
        log.error(
            "Unexpected asynchronous exception at: " +
                method.declaringClass.name + "" + method.name
        )
    }
}
