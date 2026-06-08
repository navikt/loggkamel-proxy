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

	constraints {
		implementation("org.apache.tomcat.embed:tomcat-embed-core:11.0.22") {
			because("Spring boot 4.0.6 pulls in a vulnerable version. REMOVE THIS ONCE HAVE AN UPDATED SPRING-BOOT-STARTER-WEB VERSION")
		}

		implementation("io.netty:netty-codec-http:4.2.13.Final") {
			because("ktor-server-netty-jvm:3.5.0 pulls in a vulnerable version. REMOVE THIS ONCE HAVE AN UPDATED ktor-server-netty-jvm VERSION")
		}
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