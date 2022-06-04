package com.example.cloudresourcetempates

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MyService {

    @Autowired
    private lateinit var serviceProperties: ServiceProperties
    fun message(): String {
        return serviceProperties.message
    }
}
