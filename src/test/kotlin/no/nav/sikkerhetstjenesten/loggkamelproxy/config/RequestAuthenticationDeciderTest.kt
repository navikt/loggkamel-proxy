package no.nav.sikkerhetstjenesten.loggkamelproxy.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.sun.net.httpserver.HttpServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.mock.env.MockEnvironment
import java.net.InetSocketAddress
import java.util.concurrent.atomic.AtomicReference

class RequestAuthenticationDeciderTest {

    // TODO: go through existing tests, consider where to expand or correct

    private lateinit var server: HttpServer

    @BeforeEach
    fun setUp() {
        server = HttpServer.create(InetSocketAddress("localhost", 0), 0)
    }

    @AfterEach
    fun tearDown() {
        server.stop(0)
    }

    @Test
    fun `posts bearer token to introspection endpoint and returns true`() {
        val requestMethod = AtomicReference<String>()
        val requestBody = AtomicReference<String>()

        server.createContext("/introspect") { exchange ->
            requestMethod.set(exchange.requestMethod)
            requestBody.set(exchange.requestBody.bufferedReader().use { it.readText() })

            val responseBody = """{"active":true,"subject":"123"}"""
            exchange.responseHeaders.add("Content-Type", "application/json")
            exchange.sendResponseHeaders(200, responseBody.toByteArray().size.toLong())
            exchange.responseBody.use { outputStream ->
                outputStream.write(responseBody.toByteArray())
            }
            exchange.close()
        }

        server.start()
        val endpointUrl = "http://localhost:${server.address.port}/introspect"

        val decider = RequestAuthenticationDecider(
            MockEnvironment().withProperty("NAIS_TOKEN_INTROSPECTION_ENDPOINT", endpointUrl)
        )

        assertTrue(decider.isRequestAuthenticated("Bearer my-token"))
        assertEquals("POST", requestMethod.get())

        val requestJson = ObjectMapper().readTree(requestBody.get())
        assertEquals("entra_id", requestJson["identity_provider"].asText())
        assertEquals("my-token", requestJson["token"].asText())
    }

    @Test
    fun `returns false for missing bearer header`() {
        val decider = RequestAuthenticationDecider(MockEnvironment())

        assertFalse(decider.isRequestAuthenticated(null))
        assertFalse(decider.isRequestAuthenticated("Not a bearer token"))
    }
}


