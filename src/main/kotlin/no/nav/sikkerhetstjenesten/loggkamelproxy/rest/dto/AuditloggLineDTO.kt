package no.nav.sikkerhetstjenesten.loggkamelproxy.rest.dto

import kotlin.time.Instant

class AuditloggLineDTO(metricsTimestamp: Instant ?= null, databaseName: String ?= null, tableName: String ?= null, authId: String ?= null, sqlQuery: String ?= null)