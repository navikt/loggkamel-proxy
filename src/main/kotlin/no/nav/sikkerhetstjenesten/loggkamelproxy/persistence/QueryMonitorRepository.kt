package no.nav.sikkerhetstjenesten.loggkamelproxy.persistence

import org.springframework.data.jpa.repository.JpaRepository
import kotlin.time.Instant

interface QueryMonitorRepository: JpaRepository<QueryMonitorEntity, Long> {

    fun findAllByDatabaseNameAndMetricsTimestampBetween(
        databaseName: String,
        logStartTime: Instant,
        logEndTime: Instant,
    ): List<QueryMonitorEntity>
}