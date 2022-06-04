package com.example.cloudresourcetempates

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "service")
class ServiceProperties(
    var message: String
)