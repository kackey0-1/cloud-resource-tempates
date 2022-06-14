package com.example.springbatch.entity

data class Person(
    var lastName: String? = null,
    var firstName: String? = null
) {
    override fun toString(): String = "firstName: $firstName, lastName: $lastName"
}
