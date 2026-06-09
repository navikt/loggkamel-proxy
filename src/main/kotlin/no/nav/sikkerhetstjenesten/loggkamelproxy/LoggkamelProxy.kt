package no.nav.sikkerhetstjenesten.loggkamelproxy

import no.nav.boot.conditionals.Cluster
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class LoggkamelProxy

fun main(args: Array<String>) {
	runApplication<LoggkamelProxy>(*args) {
		setAdditionalProfiles(*Cluster.profiler())
	}
}
