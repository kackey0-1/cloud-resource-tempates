package com.example.cloudresourcetempates

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "service")
@ConstructorBinding
class ServiceProperties(
    var message: String
)
