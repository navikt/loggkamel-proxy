package no.nav.sikkerhetstjenesten.loggkamelproxy.persistence

import no.nav.boot.conditionals.ConditionalOnLocalOrTest
import no.nav.sikkerhetstjenesten.loggkamelproxy.rest.dto.AuditloggLineDTO
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
@ConditionalOnLocalOrTest
class QueryMonitorAdapterMock : QueryMonitorAdapter {
    override fun getLogglinesByDatabaseAndTimePeriod(
        databaseName: String,
        logStartTime: LocalDateTime,
        logEndTime: LocalDateTime
    ): List<AuditloggLineDTO> {
        return listOf(AuditloggLineDTO(
            LocalDateTime.now(),
            "test_database_name",
            "test_table_name",
            "test_auth_id",
            "test_sql_query")
        )
    }
}