package no.nav.sikkerhetstjenesten.loggkamelproxy.rest

import no.nav.sikkerhetstjenesten.loggkamelproxy.rest.dto.AuditloggLineDTO
import no.nav.sikkerhetstjenesten.loggkamelproxy.service.AuditloggService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/auditlogg")
class AuditloggController(val auditloggService: AuditloggService) {

    @PostMapping
    fun getAuditloggsForDatabaseAndDateRange(databaseName: String,
                                             logStartTime: LocalDateTime,
                                             logEndTime: LocalDateTime,) : List<AuditloggLineDTO> {

        return auditloggService.getLogglinesByDatabaseAndTimePeriod(databaseName, logStartTime, logEndTime)
    }

    //TODO: consider limiting to only existing in DEV, or removing entirely
    @GetMapping("/sample")
    fun getSampleAuditloggs(): List<AuditloggLineDTO> {
        return auditloggService.findFirst100()
    }
}