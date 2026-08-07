package no.nav.sikkerhetstjenesten.loggkamelproxy.persistence

import no.nav.boot.conditionals.ConditionalOnFSS
import no.nav.sikkerhetstjenesten.loggkamelproxy.rest.dto.AuditloggLineDTO
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Limit
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
                                                     packetSize: Int
    ): List<AuditloggLineDTO> {
        val logglinesAsDatabaseEntities = queryMonitorRepository.findByDatabaseNameAndMetricsTimestampBetweenDatesLimitTo(databaseName, logStartTime, logEndTime, packetSize)

        return logglinesAsDatabaseEntities.map { it.toAuditloggLineDTO() }
    }

    override fun findFirst100(): List<AuditloggLineDTO> {
        return queryMonitorRepository.findBy(Limit.of(100)).map { it.toAuditloggLineDTO() }
    }
}