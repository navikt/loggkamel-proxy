package no.nav.sikkerhetstjenesten.loggkamelproxy.service

import no.nav.sikkerhetstjenesten.loggkamelproxy.persistence.QueryMonitorAdapter
import no.nav.sikkerhetstjenesten.loggkamelproxy.rest.dto.AuditloggLineDTO
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class AuditloggService(val queryMonitorAdapter: QueryMonitorAdapter) {

    fun getLogglinesByDatabaseAndTimePeriod(databaseName: String,
                                            logStartTime: LocalDateTime,
                                            logEndTime: LocalDateTime,
    ): List<AuditloggLineDTO> {
        return queryMonitorAdapter.getLogglinesByDatabaseAndTimePeriod(databaseName, logStartTime, logEndTime)
    }
}