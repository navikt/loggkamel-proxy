package no.nav.sikkerhetstjenesten.loggkamelproxy.persistence

import no.nav.sikkerhetstjenesten.loggkamelproxy.rest.dto.AuditloggLineDTO
import java.time.LocalDateTime

interface QueryMonitorAdapter {

    fun getLogglinesByDatabaseAndTimePeriod(databaseName: String,
                                            logStartTime: LocalDateTime,
                                            logEndTime: LocalDateTime,
                                            packetSize: Int
    ): List<AuditloggLineDTO>

    fun findFirst100(): List<AuditloggLineDTO>
}