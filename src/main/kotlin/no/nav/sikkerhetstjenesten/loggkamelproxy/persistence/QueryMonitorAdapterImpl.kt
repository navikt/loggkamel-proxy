package no.nav.sikkerhetstjenesten.loggkamelproxy.persistence

import no.nav.boot.conditionals.ConditionalOnFSS
import no.nav.sikkerhetstjenesten.loggkamelproxy.rest.dto.AuditloggLineDTO
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
@ConditionalOnFSS
class QueryMonitorAdapterImpl(val queryMonitorRepository: QueryMonitorRepository) : QueryMonitorAdapter {

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

        return logglinesAsDatabaseEntities.map { it.toAuditloggLineDTO() }
    }
}