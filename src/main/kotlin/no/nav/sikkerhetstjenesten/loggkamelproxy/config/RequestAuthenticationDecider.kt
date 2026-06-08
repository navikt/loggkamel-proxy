package no.nav.sikkerhetstjenesten.loggkamelproxy.config

import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class RequestAuthenticationDecider(
    private val environment: Environment,
) {

    private val log = LoggerFactory.getLogger(javaClass)
    private val restClient = RestClient.create()

    data class AuthResponse(val active: Boolean, val error: String?)

    fun isRequestAuthenticated(
        authenticationHeader: String?
    ): Boolean {
        //TODO: only for local development, remove before merging
        log.info("Authentication header is: $authenticationHeader")

        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {
            log.info("Authentication attempted with missing or misformatted header")
            return false
        }

        val bearerToken = authenticationHeader.substringAfter("Bearer ")
        //TODO: DEBUG, REMOVE AFTER DEV
        log.info("Extracted bearer token: $bearerToken")

        //TODO: pull this variable out so it's not a magic string in the code body
        val tokenIntrospectionEndpoint = environment.getProperty("NAIS_TOKEN_INTROSPECTION_ENDPOINT")
        if (tokenIntrospectionEndpoint.isNullOrBlank()) {
            log.info("NAIS_TOKEN_INTROSPECTION_ENDPOINT is missing")
            return false
        }

        val requestBody = mapOf("identity_provider" to "entra_id", "token" to bearerToken)
        //TODO: cast this as a map or json blob
        val authenticationResponse: AuthResponse = restClient.post()
            .uri(tokenIntrospectionEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Json.encodeToString(requestBody))
            .retrieve()
            .body(AuthResponse::class.java)
            ?: run {
                log.info("Token introspection endpoint returned an empty body")
                return false
            }

        //TODO: remove this debug logging, base approval on the "active" field of the response
        log.info("Token introspection response: {}", authenticationResponse.active)
        if (!authenticationResponse.active) {
            log.info("Cause for inactive token is ${authenticationResponse.error}")
        }

        return true
    }
}

