package no.nav.sikkerhetstjenesten.loggkamelproxy.persistence

import org.springframework.data.domain.Limit
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface QueryMonitorRepository: JpaRepository<QueryMonitorEntity, Long> {

    fun findAllByDatabaseNameAndMetricsTimestampBetween(
        databaseName: String,
        logStartTime: LocalDateTime,
        logEndTime: LocalDateTime,
    ): List<QueryMonitorEntity>

    fun findBy(limit: Limit): List<QueryMonitorEntity>
}