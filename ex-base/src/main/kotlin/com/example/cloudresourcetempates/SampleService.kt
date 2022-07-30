package com.example.cloudresourcetempates

import org.springframework.stereotype.Service

@Service
class SampleService(
    private val serviceProperties: ServiceProperties
) {
    fun message(): String {
        return serviceProperties.message
    }
}
