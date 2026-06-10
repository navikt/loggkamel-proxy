package no.nav.sikkerhetstjenesten.loggkamelproxy.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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

class NaisTokenIntrospector(private val environment: Environment, private val mapper: ObjectMapper): OpaqueTokenIntrospector {

    private val log = LoggerFactory.getLogger(javaClass)
    private val restClient = RestClient.create()
    private val tokenIntrospectionEndpoint = environment.getProperty("NAIS_TOKEN_INTROSPECTION_ENDPOINT")
    final val identityProvider = "entra_id"

    data class AuthResponse(val active: Boolean, val error: String?, val roles: List<String>)

    override fun introspect(bearer: String?): OAuth2AuthenticatedPrincipal? {

        if (tokenIntrospectionEndpoint.isNullOrBlank()) {
            log.info("Token introspection endpoint environment variable is missing")
            return null
        }

        val requestBody = mapOf("identity_provider" to identityProvider, "token" to bearer)
        val authenticationResponse: AuthResponse = restClient.post()
            .uri(tokenIntrospectionEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Json.encodeToString(requestBody))
            .retrieve()
            .body<AuthResponse>()
            ?: run {
                log.info("Token introspection endpoint returned an empty body")
                return null
            }

        return if (!authenticationResponse.active) {
            log.debug("Invalid token received, cause for invalid token is ${authenticationResponse.error}")
            null
        } else {
            //TODO: remove test logging
            log.info("Auth response is $authenticationResponse")
            val authenticationResponseAsMap = mapper.convertValue(authenticationResponse, object : com.fasterxml.jackson.core.type.TypeReference<Map<String, Any>>() {})
            log.info("Auth response as map: $authenticationResponseAsMap")

            val authorities = listOf(SimpleGrantedAuthority("AUTHENTICATED_NAIS_SERVICE"))
//            DefaultOAuth2AuthenticatedPrincipal(mapOf<String, String>("key" to "value"), authorities)
            DefaultOAuth2AuthenticatedPrincipal(authenticationResponseAsMap, authorities)
        }

    }
}