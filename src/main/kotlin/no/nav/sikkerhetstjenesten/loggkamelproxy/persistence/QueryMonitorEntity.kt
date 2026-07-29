package no.nav.sikkerhetstjenesten.loggkamelproxy.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.Immutable
import org.hibernate.annotations.NaturalId
import java.time.LocalDateTime
import java.util.UUID


@Entity
@Table(name = "CQM_SQLCODE_AUDIT", schema = "SYSTOOLS")
@Immutable
class QueryMonitorEntity {

    @Id
    @Column(insertable = false, updatable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var syntheticId: UUID = UUID.randomUUID()

    @NaturalId
    @Column(name = "METRICS_TIMESTAMP")
    var metricsTimestamp: LocalDateTime? = null

    @Column(name = "SMFID")
    var smfId: String? = null

    @NaturalId
    @Column(name = "DATABASE_NAME")
    var databaseName: String? = null

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

    @NaturalId
    @Column(name = "SQLTEXT")
    var sqlText: String? = null

}