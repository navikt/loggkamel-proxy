package no.nav.sikkerhetstjenesten.loggkamelproxy.rest.dto

import java.time.LocalDateTime

data class AuditloggLineDTO(val metricsTimestamp: LocalDateTime, val databaseName: String, val tableName: String, val authId: String, val sqlQuery: String)