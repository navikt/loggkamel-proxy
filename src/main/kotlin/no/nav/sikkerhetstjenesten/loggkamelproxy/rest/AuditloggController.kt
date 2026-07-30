package no.nav.sikkerhetstjenesten.loggkamelproxy.rest

import no.nav.sikkerhetstjenesten.loggkamelproxy.rest.dto.AuditloggLineDTO
import no.nav.sikkerhetstjenesten.loggkamelproxy.service.AuditloggService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/v1/auditlogg")
class AuditloggController(val auditloggService: AuditloggService) {

    var log = LoggerFactory.getLogger(AuditloggController::class.java)

    @PostMapping
    fun getAuditloggsForDatabaseAndDateRange(databaseName: String,
                                             logStartTime: LocalDateTime,
                                             logEndTime: LocalDateTime,) : List<AuditloggLineDTO> {

        return auditloggService.getLogglinesByDatabaseAndTimePeriod(databaseName, logStartTime, logEndTime)
    }

    @GetMapping("/sample")
    fun getSampleAuditloggs(): List<AuditloggLineDTO> {
        return auditloggService.findFirst100()
    }
}