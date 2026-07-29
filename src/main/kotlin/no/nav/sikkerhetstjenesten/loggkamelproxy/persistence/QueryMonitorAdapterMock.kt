package no.nav.sikkerhetstjenesten.loggkamelproxy.persistence

import no.nav.boot.conditionals.ConditionalOnLocalOrTest
import no.nav.sikkerhetstjenesten.loggkamelproxy.rest.dto.AuditloggLineDTO
import org.springframework.stereotype.Component
import kotlin.time.Clock
import kotlin.time.Instant

@Component
@ConditionalOnLocalOrTest
class QueryMonitorAdapterMock : QueryMonitorAdapter {
    override fun getLogglinesByDatabaseAndTimePeriod(
        databaseName: String,
        logStartTime: Instant,
        logEndTime: Instant
    ): List<AuditloggLineDTO> {
        return listOf(AuditloggLineDTO(
            Clock.System.now(),
            "test_database_name",
            "test_table_name",
            "test_auth_id",
            "test_sql_query")
        )
    }
}