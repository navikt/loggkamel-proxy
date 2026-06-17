package no.nav.sikkerhetstjenesten.loggkamelproxy.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime


@Entity
class QueryMonitorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(name = "METRICS_TIMESTAMP")
    val metricsTimestamp: LocalDateTime? = null

    @Column(name = "SMFID")
    val smfId: String? = null

    @Column(name = "DATABASE_NAME")
    val databaseName: String? = null

    @Column(name = "PAGESET_NAME")
    val pagesetName: String? = null

    @Column(name = "OBJECT_CREATOR")
    val objectCreator: String? = null

    @Column(name = "OBJECT_NAME")
    val objectName: String? = null

    @Column(name = "TBCREATOR")
    val tbCreator: String? = null

    @Column(name = "TBNAME")
    val tbName: String? = null

    @Column(name = "AUTHID")
    val authId: String? = null

    @Column(name = "SQLTEXT")
    val sqlText: String? = null

    //TODO: there will be an extra field here being added by kjell-anders for which kind of query this is


}