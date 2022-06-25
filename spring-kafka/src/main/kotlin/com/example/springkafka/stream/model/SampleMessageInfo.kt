package com.example.springkafka.stream.model

data class SampleMessageInfo(
    val message: String,
    val retryCount: Int = 0
) {
    fun incrementRetryCount() = copy(
        retryCount = retryCount + 1
    )
}
