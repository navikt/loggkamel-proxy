package no.nav.sikkerhetstjenesten.loggkamelproxy.rest

import no.nav.sikkerhetstjenesten.loggkamelproxy.rest.dto.AuditloggLineDTO
import no.nav.sikkerhetstjenesten.loggkamelproxy.service.AuditloggService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/v1/auditlogg")
class AuditloggController(val auditloggService: AuditloggService) {

    var log = LoggerFactory.getLogger(AuditloggController::class.java)

    //TODO: manual test, confirm this takes these as part of the request body
    @PostMapping
    fun testEndpoint(databaseName: String,
                     logStartTime: LocalDateTime,
                     logEndTime: LocalDateTime,) : List<AuditloggLineDTO> {

        log.info("Request received with databaseName: $databaseName, logStartTime: $logStartTime, logEndTime: $logEndTime")
//        log.info("logStarttime as instant: ${logStartTime}")
//        log.info("logEndtime as instant: ${logEndTime}")

        return auditloggService.getLogglinesByDatabaseAndTimePeriod(databaseName, logStartTime, logEndTime)
    }
}