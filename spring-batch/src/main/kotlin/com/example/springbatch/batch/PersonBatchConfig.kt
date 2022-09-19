package com.example.springbatch.batch

import com.example.springbatch.batch.impl.PersonItemProcessor
import com.example.springbatch.entity.Person
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.core.task.TaskExecutor
import javax.sql.DataSource

@Configuration
class PersonBatchConfig(
    private val dataSource: DataSource,
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val asyncTaskExecutor: TaskExecutor
) {

    // end::setup[]
    // tag::readerwriterprocessor[]
    @Bean
    fun personReader(): FlatFileItemReader<Person> {
        return FlatFileItemReaderBuilder<Person>()
            .name("personItemReader")
            .resource(ClassPathResource("sample-data.csv"))
            .delimited()
            .names(*arrayOf("firstName", "lastName"))
            .fieldSetMapper(object : BeanWrapperFieldSetMapper<Person>() {
                init {
                    setTargetType(Person::class.java)
                }
            })
            .build()
    }

    @Bean
    fun personProcessor(): PersonItemProcessor {
        return PersonItemProcessor()
    }

    @Bean
    fun personWriter(): JdbcBatchItemWriter<Person> {
        return JdbcBatchItemWriterBuilder<Person>()
            .itemSqlParameterSourceProvider(BeanPropertyItemSqlParameterSourceProvider<Person>())
            .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
            .dataSource(dataSource)
            .build()
    }

    // end::readerwriterprocessor[]
    // tag::jobstep[]
    @Bean
    fun importUserJob(listener: JobCompletionNotificationListener): Job {
        return jobBuilderFactory["importUserJob"]
            .incrementer(RunIdIncrementer())
            .listener(listener)
            .flow(personJobStep1())
            .end()
            .build()
    }

    @Bean
    fun personJobStep1(): Step {
        return stepBuilderFactory["step1"]
            .chunk<Person, Person>(10)
            .reader(personReader())
            .processor(personProcessor())
            .writer(personWriter())
            .taskExecutor(asyncTaskExecutor)
            .build()
    } // end::jobstep[]
}
