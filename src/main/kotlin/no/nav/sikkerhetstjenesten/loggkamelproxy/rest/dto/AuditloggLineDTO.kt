package no.nav.sikkerhetstjenesten.loggkamelproxy.rest.dto

import java.time.LocalDateTime

class AuditloggLineDTO(metricsTimestamp: LocalDateTime ?= null, databaseName: String ?= null, tableName: String ?= null, authId: String ?= null, sqlQuery: String ?= null)