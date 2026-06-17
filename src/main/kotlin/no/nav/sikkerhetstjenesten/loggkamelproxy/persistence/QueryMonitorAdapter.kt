package no.nav.sikkerhetstjenesten.loggkamelproxy.persistence

import no.nav.sikkerhetstjenesten.loggkamelproxy.rest.dto.AuditloggLineDTO
import java.time.LocalDateTime

interface QueryMonitorAdapter {

    fun getLogglinesByDatabaseAndTimePeriod(databaseName: String,
                                            logStartTime: LocalDateTime,
                                            logEndTime: LocalDateTime,
    ): List<AuditloggLineDTO>
}