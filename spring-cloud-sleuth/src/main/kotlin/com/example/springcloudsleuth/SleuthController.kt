package com.example.springcloudsleuth

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.Executor

@RestController
class SleuthController(
    private val sleuthService: SleuthService,
    private val executor: Executor,
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @GetMapping("/")
    fun helloSleuth(): String {
        logger.info("Hello Sleuth")
        return "success"
    }

    @GetMapping("/same-span")
    @Throws(InterruptedException::class)
    fun helloSleuthSameSpan(): String {
        logger.info("Same Span")
        sleuthService.doSomeWorkSameSpan()
        return "success"
    }

    @GetMapping("/new-span")
    @Throws(InterruptedException::class)
    fun helloSleuthNewSpan(): String {
        logger.info("New Span")
        sleuthService.doSomeWorkNewSpan()
        return "success"
    }

    @GetMapping("/new-thread")
    fun helloSleuthNewThread(): String {
        logger.info("New Thread")
        val runnable = Runnable {
            try {
                Thread.sleep(1000L)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            logger.info("I'm inside the new thread - with a new span")
        }
        executor.execute(runnable)
        logger.info("I'm done - with the original span")
        return "success"
    }

    @GetMapping("/async")
    @Throws(InterruptedException::class)
    fun helloSleuthAsync(): String {
        logger.info("Before Async Method Call")
        sleuthService.asyncMethod()
        logger.info("After Async Method Call")
        return "success"
    }
}
