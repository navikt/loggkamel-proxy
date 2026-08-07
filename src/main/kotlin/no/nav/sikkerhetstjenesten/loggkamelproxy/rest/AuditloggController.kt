package no.nav.sikkerhetstjenesten.loggkamelproxy.rest

import no.nav.sikkerhetstjenesten.loggkamelproxy.rest.dto.AuditloggLineDTO
import no.nav.sikkerhetstjenesten.loggkamelproxy.service.AuditloggService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/auditlogg")
class AuditloggController(val auditloggService: AuditloggService) {

    var packetSizeLimit = 5000

    data class AuditloggRequest(val databaseName: String, val logStartTime: LocalDateTime, val logEndTime: LocalDateTime, val packetSize: Int)

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAuditloggsForDatabaseAndDateRange(@RequestBody request: AuditloggRequest) : List<AuditloggLineDTO> {
        if (request.packetSize > packetSizeLimit) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Requested packet size above maximum size limit: $packetSizeLimit")
        }

        return auditloggService.getLogglinesByDatabaseAndTimePeriod(request.databaseName, request.logStartTime, request.logEndTime, request.packetSize)
    }

}