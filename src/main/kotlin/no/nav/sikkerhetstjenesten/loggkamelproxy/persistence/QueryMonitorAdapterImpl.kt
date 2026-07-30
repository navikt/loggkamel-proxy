package no.nav.sikkerhetstjenesten.loggkamelproxy.persistence

import kotlinx.serialization.json.Json
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
        log.info("About to make request to repository")

        val logglinesAsDatabaseEntities = queryMonitorRepository.findAllByDatabaseNameAndMetricsTimestampBetween(databaseName, logStartTime, logEndTime)

        log.info("Found ${logglinesAsDatabaseEntities.size} logglines")
        log.info("First entry is: ${Json.encodeToString(QueryMonitorEntity.serializer(), logglinesAsDatabaseEntities.first())}")

        return logglinesAsDatabaseEntities.map { it.toAuditloggLineDTO() }
    }
}