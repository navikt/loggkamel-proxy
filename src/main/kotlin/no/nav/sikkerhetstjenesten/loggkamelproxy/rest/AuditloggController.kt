package no.nav.sikkerhetstjenesten.loggkamelproxy.rest

import no.nav.sikkerhetstjenesten.loggkamelproxy.rest.dto.AuditloggLineDTO
import no.nav.sikkerhetstjenesten.loggkamelproxy.service.AuditloggService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/v1/auditlogg")
class AuditloggController(val auditloggService: AuditloggService) {

    //TODO: manual test, confirm this takes these as part of the request body
    @PostMapping
    fun testEndpoint(databaseName: String,
                     logStartTime: LocalDateTime,
                     logEndTime: LocalDateTime,) : List<AuditloggLineDTO> {
        return auditloggService.getLogglinesByDatabaseAndTimePeriod(databaseName, logStartTime, logEndTime)
    }
}