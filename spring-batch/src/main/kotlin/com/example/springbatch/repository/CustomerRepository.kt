package com.example.springbatch.repository

import com.example.springbatch.entity.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository : JpaRepository<Customer, Int>
