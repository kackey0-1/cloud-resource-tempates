package com.example.springbatch.batch.impl

import com.example.springbatch.entity.Person
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor

class PersonItemProcessor : ItemProcessor<Person, Person> {

    private val log = LoggerFactory.getLogger(PersonItemProcessor::class.java)

    override fun process(person: Person): Person {
        val firstName: String = person.firstName!!.uppercase()
        val lastName: String = person.lastName!!.uppercase()
        val transformedPerson = Person(firstName, lastName)
        log.info("Converting ($person) into ($transformedPerson)")
        return transformedPerson
    }
}
