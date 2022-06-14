package com.example.springbatch.config

import com.example.springbatch.batchimpl.CustomerProcessor
import com.example.springbatch.entity.Customer
import com.example.springbatch.repository.CustomerRepository
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.data.RepositoryItemWriter
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.LineMapper
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.batch.item.file.mapping.DefaultLineMapper
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.core.task.TaskExecutor

@Configuration
@EnableBatchProcessing
class CustomerBatchConfig(
    private var jobBuilderFactory: JobBuilderFactory,
    private var stepBuilderFactory: StepBuilderFactory,
    private var customerRespository: CustomerRepository,
    private val asyncTaskExecutor: TaskExecutor
) {

    @Bean
    fun customerReader(): FlatFileItemReader<Customer> {
        val itemReader = FlatFileItemReader<Customer>()
        itemReader.setResource(FileSystemResource("src/main/resources/customers.csv"))
        itemReader.setName("csvReader")
        itemReader.setLinesToSkip(1)
        itemReader.setLineMapper(lineMapper())
        return itemReader
    }

    @Bean
    fun lineMapper(): LineMapper<Customer> {
        val lineMapper = DefaultLineMapper<Customer>()

        val lineTokenizer = DelimitedLineTokenizer()
        lineTokenizer.setDelimiter(",")
        lineTokenizer.setStrict(false)
        lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob")

        val fieldSetMapper = BeanWrapperFieldSetMapper<Customer>()
        fieldSetMapper.setTargetType(Customer::class.java)
        lineMapper.setFieldSetMapper(fieldSetMapper)
        lineMapper.setLineTokenizer(lineTokenizer)

        return lineMapper
    }

    @Bean
    fun customerProcessor(): CustomerProcessor = CustomerProcessor()

    @Bean
    fun customerWriter(): RepositoryItemWriter<Customer> {
        val writer = RepositoryItemWriter<Customer>()
        writer.setRepository(customerRespository)
        writer.setMethodName("save")
        return writer
    }

    @Bean
    fun customerStep1(): Step {
        return stepBuilderFactory.get("csv-step").chunk<Customer, Customer>(10)
            .reader(customerReader())
            .processor(customerProcessor())
            .writer(customerWriter())
            .taskExecutor(asyncTaskExecutor)
            .build()
    }

    @Bean("customerJob")
    fun customerJob(): Job {
        return jobBuilderFactory.get("importCustomers")
            .flow(customerStep1())
            .end()
            // .next()
            .build()
    }
}
