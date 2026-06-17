package no.nav.sikkerhetstjenesten.loggkamelproxy.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime


@Entity
@Table(name = "CQM_SQLCODE_AUDIT")
class QueryMonitorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "METRICS_TIMESTAMP")
    var metricsTimestamp: LocalDateTime? = null

    @Column(name = "SMFID")
    var smfId: String? = null

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

    @Column(name = "SQLTEXT")
    var sqlText: String? = null

}