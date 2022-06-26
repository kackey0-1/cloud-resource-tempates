package com.example.awsapprunner.entity

import javax.persistence.Column
import javax.persistence.MappedSuperclass
import javax.validation.constraints.NotEmpty

@MappedSuperclass
class Person(
    @NotEmpty
    @Column(name = "first_name")
    var firstName: String? = null,
    @NotEmpty
    @Column(name = "last_name")
    var lastName: String? = null
) : BaseEntity(null)
