package no.nav.sikkerhetstjenesten.loggkamelproxy.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.sun.net.httpserver.HttpServer
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.mock.env.MockEnvironment
import java.net.InetSocketAddress
import java.util.concurrent.atomic.AtomicReference

class RequestAuthenticationDeciderTest {

    private lateinit var server: HttpServer

    @BeforeEach
    fun setUp() {
        server = HttpServer.create(InetSocketAddress("localhost", 0), 0)
    }

    @AfterEach
    fun tearDown() {
        server.stop(0)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `posts bearer token to introspection endpoint and returns introspection active value`(isAccepted: Boolean) {
        val requestMethod = AtomicReference<String>()
        val requestBody = AtomicReference<String>()

        server.createContext("/introspect") { exchange ->
            requestMethod.set(exchange.requestMethod)
            requestBody.set(exchange.requestBody.bufferedReader().use { it.readText() })

            val responseBodyAsMap = mapOf("active" to isAccepted)
            val responseBodyAsString = Json.encodeToString(responseBodyAsMap)
            exchange.responseHeaders.add("Content-Type", "application/json")
            exchange.sendResponseHeaders(200, responseBodyAsString.toByteArray().size.toLong())
            exchange.responseBody.use { outputStream ->
                outputStream.write(responseBodyAsString.toByteArray())
            }
            exchange.close()
        }

        server.start()
        val endpointUrl = "http://localhost:${server.address.port}/introspect"

        val decider = RequestAuthenticationDecider(
            MockEnvironment().withProperty("NAIS_TOKEN_INTROSPECTION_ENDPOINT", endpointUrl)
        )

        val passedInToken = "my-token"
        assertEquals(isAccepted, decider.isRequestAuthenticated("Bearer $passedInToken"))

        val requestJson = ObjectMapper().readTree(requestBody.get())
        assertEquals(decider.identityProvider, requestJson["identity_provider"].asText())
        assertEquals(passedInToken, requestJson["token"].asText())
    }

    @Test
    fun `returns false for missing or invalid bearer token`() {
        val decider = RequestAuthenticationDecider(MockEnvironment())

        assertFalse(decider.isRequestAuthenticated(null))
        assertFalse(decider.isRequestAuthenticated("Not a bearer token"))
    }
}


