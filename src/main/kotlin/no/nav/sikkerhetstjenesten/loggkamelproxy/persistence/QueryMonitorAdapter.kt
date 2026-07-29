package no.nav.sikkerhetstjenesten.loggkamelproxy.persistence

import no.nav.sikkerhetstjenesten.loggkamelproxy.rest.dto.AuditloggLineDTO
import kotlin.time.Instant

interface QueryMonitorAdapter {

    fun getLogglinesByDatabaseAndTimePeriod(databaseName: String,
                                            logStartTime: Instant,
                                            logEndTime: Instant,
    ): List<AuditloggLineDTO>
}