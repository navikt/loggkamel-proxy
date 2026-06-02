package no.nav.sikkerhetstjenesten.loggkamelproxy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LoggkamelProxy

fun main(args: Array<String>) {
	runApplication<LoggkamelProxy>(*args)
}
