package com.example.springbatch.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "customers")
data class Customer(
    @Id @Column(name = "customer_id") var id: Long? = null,
    @Column(name = "first_name") var firstName: String = "",
    @Column(name = "last_name") var lastName: String = "",
    @Column(name = "email") var email: String = "",
    @Column(name = "gender") var gender: String = "",
    @Column(name = "contact") var contactNo: String = "",
    @Column(name = "country") var country: String = "",
    @Column(name = "dob") var dob: String = "",
) : Serializable
