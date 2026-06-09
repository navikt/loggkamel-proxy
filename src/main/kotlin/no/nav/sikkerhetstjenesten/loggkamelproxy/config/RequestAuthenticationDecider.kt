package no.nav.sikkerhetstjenesten.loggkamelproxy.config

import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class RequestAuthenticationDecider(
    private val environment: Environment,
) {

    private val log = LoggerFactory.getLogger(javaClass)
    private val restClient = RestClient.create()
    private val tokenIntrospectionEndpoint = environment.getProperty("NAIS_TOKEN_INTROSPECTION_ENDPOINT")
    final val identityProvider = "entra_id"

    data class AuthResponse(val active: Boolean, val error: String?)

    fun isRequestAuthenticated(
        authenticationHeader: String?
    ): Boolean {
        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {
            log.info("Authentication attempted with missing or misformatted header")
            return false
        }

        val bearerToken = authenticationHeader.substringAfter("Bearer ")

        if (tokenIntrospectionEndpoint.isNullOrBlank()) {
            log.info("Token introspection endpoint environment variable is missing")
            return false
        }

        val requestBody = mapOf("identity_provider" to identityProvider, "token" to bearerToken)
        val authenticationResponse: AuthResponse = restClient.post()
            .uri(tokenIntrospectionEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Json.encodeToString(requestBody))
            .retrieve()
            .body<AuthResponse>()
            ?: run {
                log.info("Token introspection endpoint returned an empty body")
                return false
            }

        if (!authenticationResponse.active) {
            log.debug("Invalid token received, cause for invalid token is ${authenticationResponse.error}")
        }

        return authenticationResponse.active
    }
}

