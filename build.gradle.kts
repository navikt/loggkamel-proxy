plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.spring)
	alias(libs.plugins.spring.boot)
	application
}

application {
	mainClass.set("no.nav.sikkerhetstjenesten.loggkamelproxy.LoggkamelProxyKt")
	applicationName = "app"
}

kotlin {
	jvmToolchain(25)
}

dependencies {
	implementation(libs.bundles.ktor)
	implementation(libs.bundles.logging)
	implementation(libs.bundles.spring)
	implementation(libs.bundles.springboot)
	implementation(libs.bundles.kotlin)
	implementation(libs.bundles.openapi)

	implementation(libs.bundles.nav)

	testImplementation(platform(libs.junit.bom))
	testImplementation(libs.bundles.test)

	constraints {
		// Pin non-vulnerable versions here
	}
}

tasks {
	withType<Test> {
		useJUnitPlatform()
		testLogging {
			showExceptions = true
		}
	}
}