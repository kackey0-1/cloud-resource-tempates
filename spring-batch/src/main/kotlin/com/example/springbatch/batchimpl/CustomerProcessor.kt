package com.example.springbatch.batchimpl

import com.example.springbatch.entity.Customer
import org.springframework.batch.item.ItemProcessor

class CustomerProcessor : ItemProcessor<Customer, Customer> {
    override fun process(item: Customer): Customer? {
        return if (item.country == "United States") item else null
    }
}
