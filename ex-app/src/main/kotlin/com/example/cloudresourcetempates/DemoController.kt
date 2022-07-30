package com.example.cloudresourcetempates

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DemoController(
    private val myService: SampleService
) {
    @GetMapping("/")
    fun home(): String {
        return myService.message()
    }
}
