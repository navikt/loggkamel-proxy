package no.nav.sikkerhetstjenesten.loggkamelproxy.persistence

import java.time.LocalDateTime
import org.springframework.data.jpa.repository.JpaRepository

interface QueryMonitorRepository: JpaRepository<QueryMonitorEntity, Long> {

    fun findAllByDatabaseNameAndMetricsTimestampBetween(
        databaseName: String,
        logStartTime: LocalDateTime,
        logEndTime: LocalDateTime,
    ): List<QueryMonitorEntity>
}