package com.example.springbatch.batch.customer

import com.example.springbatch.entity.Person
import org.slf4j.LoggerFactory
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.listener.JobExecutionListenerSupport
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.util.function.Consumer

@Component
class JobCompletionNotificationListener(
    private val jdbcTemplate: JdbcTemplate
) : JobExecutionListenerSupport() {
    private val log = LoggerFactory.getLogger(JobCompletionNotificationListener::class.java)

    override fun afterJob(jobExecution: JobExecution) {
        if (jobExecution.status == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results")
            jdbcTemplate.query<Any>(
                "SELECT first_name, last_name FROM people"
            ) { rs: ResultSet, row: Int ->
                Person(
                    rs.getString(1),
                    rs.getString(2)
                )
            }.forEach(Consumer { person: Any -> log.info("Found <$person> in the database.") })
        }
    }
}
