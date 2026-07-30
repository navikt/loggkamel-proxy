package no.nav.sikkerhetstjenesten.loggkamelproxy.rest.dto

import java.time.LocalDateTime

data class AuditloggLineDTO(val metricsTimestamp: LocalDateTime ?= null, val databaseName: String ?= null, val tableName: String ?= null, val authId: String ?= null, val sqlQuery: String ?= null)