package no.nav.sikkerhetstjenesten.loggkamelproxy.persistence

import org.springframework.data.domain.Limit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface QueryMonitorRepository: JpaRepository<QueryMonitorEntity, Long> {

    @Query("SELECT e from QueryMonitorEntity e where e.databaseName = :databaseName and e.metricsTimestamp between :logStartTime and :logEndTime order by e.metricsTimestamp asc limit :limit")
    fun findByDatabaseNameAndMetricsTimestampBetweenDatesLimitTo(
        databaseName: String,
        logStartTime: LocalDateTime,
        logEndTime: LocalDateTime,
        limit: Int
    ): List<QueryMonitorEntity>

    fun findBy(limit: Limit): List<QueryMonitorEntity>
}