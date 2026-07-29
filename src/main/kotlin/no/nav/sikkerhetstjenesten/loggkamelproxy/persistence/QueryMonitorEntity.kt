package no.nav.sikkerhetstjenesten.loggkamelproxy.persistence

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.Immutable
import java.io.Serializable
import kotlin.time.Instant


@Embeddable
@kotlinx.serialization.Serializable
data class QueryMonitorId(
    @Column(name = "METRICS_TIMESTAMP", insertable = false, updatable = false)
    var metricsTimestamp: Instant? = null,

    @Column(name = "DATABASE_NAME", insertable = false, updatable = false)
    var databaseName: String? = null,

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
@kotlinx.serialization.Serializable
class QueryMonitorEntity {

    @EmbeddedId
    var id: QueryMonitorId? = null

    @Column(name = "METRICS_TIMESTAMP", insertable = false, updatable = false)
    var metricsTimestamp: Instant? = null

    @Column(name = "DATABASE_NAME", insertable = false, updatable = false)
    var databaseName: String? = null

    @Column(name = "SQLTEXT", insertable = false, updatable = false)
    var sqlText: String? = null

    @Column(name = "SMFID")
    var smfId: String? = null

    @Column(name = "PAGESET_NAME")
    var pagesetName: String? = null

    @Column(name = "OBJECT_CREATOR")
    var objectCreator: String? = null

    @Column(name = "OBJECT_NAME")
    var objectName: String? = null

    @Column(name = "TBCREATOR")
    var tbCreator: String? = null

    @Column(name = "TBNAME")
    var tbName: String? = null

    @Column(name = "AUTHID")
    var authId: String? = null

}