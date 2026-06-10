package no.nav.sikkerhetstjenesten.loggkamelproxy.auth

import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

class NaisTokenIntrospector(private val environment: Environment,): OpaqueTokenIntrospector {

    private val log = LoggerFactory.getLogger(javaClass)
    private val restClient = RestClient.create()
    private val tokenIntrospectionEndpoint = environment.getProperty("NAIS_TOKEN_INTROSPECTION_ENDPOINT")
    final val identityProvider = "entra_id"

    override fun introspect(bearer: String?): OAuth2AuthenticatedPrincipal? {

        if (tokenIntrospectionEndpoint.isNullOrBlank()) {
            log.info("Token introspection endpoint environment variable is missing")
            return null
        }

        val requestBody = mapOf("identity_provider" to identityProvider, "token" to bearer)
        val authenticationResponse: Map<String, Any> = restClient.post()
            .uri(tokenIntrospectionEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Json.encodeToString(requestBody))
            .retrieve()
            .body<Map<String, Any>>()
            ?: run {
                log.info("Token introspection endpoint returned an empty body")
                return null
            }

        return if (!(authenticationResponse["active"] as? Boolean ?: false)) {
            log.debug("Invalid token received, cause for invalid token is ${authenticationResponse["error"] as? String ?: "unknown"}")
            null
        } else {
            //TODO: remove test logging
            log.info("Auth response is $authenticationResponse")

            val authorities = listOf(SimpleGrantedAuthority("AUTHENTICATED_NAIS_SERVICE"))
            DefaultOAuth2AuthenticatedPrincipal(authenticationResponse, authorities)
        }

    }
}