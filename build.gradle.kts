plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.spring)
	alias(libs.plugins.spring.boot)
	alias(libs.plugins.spring.dependency)
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

	implementation(libs.bundles.springboot)
	implementation(libs.bundles.kotlin)
	implementation(libs.bundles.openapi)

	testImplementation(platform(libs.junit.bom))
	testImplementation(libs.bundles.test)
}

tasks {
	withType<Test> {
		useJUnitPlatform()
		testLogging {
			showExceptions = true
		}
	}
}