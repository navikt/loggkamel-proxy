package no.nav.sikkerhetstjenesten.loggkamelproxy.persistence

import no.nav.boot.conditionals.ConditionalOnFSS
import no.nav.sikkerhetstjenesten.loggkamelproxy.rest.dto.AuditloggLineDTO
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
@ConditionalOnFSS
class QueryMonitorAdapterImpl(val queryMonitorRepository: QueryMonitorRepository) : QueryMonitorAdapter {

    var log = LoggerFactory.getLogger(QueryMonitorAdapterImpl::class.java)

    fun QueryMonitorEntity.toAuditloggLineDTO(): AuditloggLineDTO {
        return AuditloggLineDTO(
            this.metricsTimestamp, this.databaseName, this.tbName, this.authId, this.sqlText
        )
    }

    override fun getLogglinesByDatabaseAndTimePeriod(databaseName: String,
                                                     logStartTime: LocalDateTime,
                                                     logEndTime: LocalDateTime,
    ): List<AuditloggLineDTO> {
        val logglinesAsDatabaseEntities = queryMonitorRepository.findAllByDatabaseNameAndMetricsTimestampBetween(databaseName, logStartTime, logEndTime)

        log.info("Found ${logglinesAsDatabaseEntities.size} logglines")

        return logglinesAsDatabaseEntities.map { it.toAuditloggLineDTO() }
    }
}