package no.nav.sikkerhetstjenesten.loggkamelproxy.persistence

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Lob
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import org.hibernate.annotations.Immutable
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Embeddable
data class QueryMonitorId(
    @Column(name = "METRICS_TIMESTAMP", insertable = false, updatable = false, columnDefinition = "TIMESTAMP")
    var metricsTimestamp: LocalDateTime? = null,

    @Column(name = "DATABASE_NAME", insertable = false, updatable = false, columnDefinition = "CHAR(8)")
    var databaseName: String? = null,

    @Lob
    @Column(name = "SQLTEXT", insertable = false, updatable = false)
    var sqlText: String? = null
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}


@Entity
@Table(name = "CQM_SQLCODE_AUDIT", schema = "SYSTOOLS")
@Immutable
class QueryMonitorEntity(

    @EmbeddedId
    var id: QueryMonitorId? = null,

    @field:NotBlank
    @Column(
        name = "METRICS_TIMESTAMP",
        insertable = false,
        updatable = false,
        columnDefinition = "TIMESTAMP",
        nullable = false
    )
    var metricsTimestamp: LocalDateTime,

    @field:NotBlank
    @Column(name = "DATABASE_NAME", insertable = false, updatable = false, nullable = false, columnDefinition = "CHAR(8)")
    var databaseName: String,

    @field:NotBlank
    @Lob
    @Column(name = "SQLTEXT", insertable = false, updatable = false, nullable = false)
    var sqlText: String,

    @Column(name = "SMFID")
    var smfId: String? = null,

    @Column(name = "PAGESET_NAME")
    var pagesetName: String? = null,

    @Column(name = "OBJECT_CREATOR")
    var objectCreator: String? = null,

    @Column(name = "OBJECT_NAME")
    var objectName: String? = null,

    @Column(name = "TBCREATOR")
    var tbCreator: String? = null,

    @field:NotBlank
    @Column(name = "TBNAME", nullable = false)
    var tbName: String,

    @field:NotBlank
    @Column(name = "AUTHID", nullable = false, columnDefinition = "CHAR(8)")
    var authId: String
) {

    override fun toString(): String {
        val ts = metricsTimestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        return "QueryMonitorEntity(" +
                "metricsTimestamp=$ts, " +
                "databaseName=$databaseName, " +
                "tbName=$tbName, " +
                "authId=$authId, " +
                "sqlText=$sqlText" +
                ")"
    }

}