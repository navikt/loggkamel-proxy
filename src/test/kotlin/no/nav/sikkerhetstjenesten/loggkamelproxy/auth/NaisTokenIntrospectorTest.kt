package no.nav.sikkerhetstjenesten.loggkamelproxy.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.sun.net.httpserver.HttpServer
import no.nav.boot.conditionals.Cluster
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.mock.env.MockEnvironment
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionException
import org.springframework.web.client.RestClient
import java.net.InetSocketAddress

class NaisTokenIntrospectorTest {

    private var server: HttpServer? = null

    @AfterEach
    fun tearDown() {
        server?.stop(0)
    }

    @Test
    fun `returns authenticated principal locally even with invalid introspection endpoint`() {
        val introspector = createIntrospector(
            environment = MockEnvironment().withProperty("NAIS_TOKEN_INTROSPECTION_ENDPOINT", "http://localhost:1/not-used"),
            cluster = Cluster.LOCAL
        )

        val principal = introspector.introspect("bearer-token")

        assertNotNull(principal)
        assertEquals("value", principal?.attributes?.get("key"))
    }

    @Test
    fun `throws BadOpaqueTokenException when introspection endpoint is missing`() {
        val introspector = createIntrospector(environment = MockEnvironment(), cluster = Cluster.PROD_FSS)

        assertThrows(OAuth2IntrospectionException::class.java) {
            introspector.introspect("bearer-token")
        }
    }

    @Test
    fun `throws BadOpaqueTokenException when authentication response body is empty`() {
        val endpointUrl = startServerWithNoContent()
        val introspector = createIntrospector(
            environment = MockEnvironment().withProperty("NAIS_TOKEN_INTROSPECTION_ENDPOINT", endpointUrl),
            cluster = Cluster.PROD_FSS
        )

        assertThrows(OAuth2IntrospectionException::class.java) {
            introspector.introspect("bearer-token")
        }
    }

    @Test
    fun `throws BadOpaqueTokenException when authentication response is not active`() {
        val endpointUrl = startServerWithJson(mapOf("active" to false, "error" to "invalid_token", "roles" to listOf("role-a")))
        val introspector = createIntrospector(
            environment = MockEnvironment().withProperty("NAIS_TOKEN_INTROSPECTION_ENDPOINT", endpointUrl),
            cluster = Cluster.PROD_FSS
        )

        assertThrows(BadOpaqueTokenException::class.java) {
            introspector.introspect("bearer-token")
        }
    }

    @Test
    fun `returns authenticated principal with claims when authentication response is active`() {
        val grantedRoles = listOf("role-a", "role-b")
        val endpointUrl = startServerWithJson(mapOf("active" to true, "error" to null, "roles" to grantedRoles))
        val introspector = createIntrospector(
            environment = MockEnvironment().withProperty("NAIS_TOKEN_INTROSPECTION_ENDPOINT", endpointUrl),
            cluster = Cluster.PROD_FSS
        )

        val principal = introspector.introspect("bearer-token")

        assertNotNull(principal)
        assertEquals(true, principal?.attributes?.get("active"))
        assertEquals(listOf("role-a", "role-b"), principal?.attributes?.get("roles"))
    }

    private fun createIntrospector(environment: MockEnvironment, cluster: Cluster): NaisTokenIntrospector {
        return NaisTokenIntrospector(
            environment = environment,
            mapper = ObjectMapper(),
            restClient = RestClient.create(),
            clusterProvider = { cluster }
        )
    }

    private fun startServerWithNoContent(): String {
        server = HttpServer.create(InetSocketAddress("localhost", 0), 0).apply {
            createContext("/introspect") { exchange ->
                exchange.requestBody.bufferedReader().use { it.readText() }
                exchange.sendResponseHeaders(204, -1)
                exchange.close()
            }
            start()
        }

        return "http://localhost:${server!!.address.port}/introspect"
    }

    private fun startServerWithJson(responseBodyAsMap: Map<String, Any?>): String {
        server = HttpServer.create(InetSocketAddress("localhost", 0), 0).apply {
            createContext("/introspect") { exchange ->
                exchange.requestBody.bufferedReader().use { it.readText() }
                val responseBodyAsString = ObjectMapper().writeValueAsString(responseBodyAsMap)
                exchange.responseHeaders.add("Content-Type", "application/json")
                exchange.sendResponseHeaders(200, responseBodyAsString.toByteArray().size.toLong())
                exchange.responseBody.use { outputStream ->
                    outputStream.write(responseBodyAsString.toByteArray())
                }
                exchange.close()
            }
            start()
        }

        return "http://localhost:${server!!.address.port}/introspect"
    }
}




