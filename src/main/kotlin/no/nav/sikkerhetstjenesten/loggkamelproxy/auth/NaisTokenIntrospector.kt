package no.nav.sikkerhetstjenesten.loggkamelproxy.auth

import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.boot.conditionals.Cluster
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionException
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

class NaisTokenIntrospector(
    private val environment: Environment,
    private val mapper: ObjectMapper,
    private val restClient: RestClient = RestClient.create(),
    private val clusterProvider: () -> Cluster = { Cluster.currentCluster() }
): OpaqueTokenIntrospector {

    private val log = LoggerFactory.getLogger(javaClass)
    private val tokenIntrospectionEndpoint = environment.getProperty("NAIS_TOKEN_INTROSPECTION_ENDPOINT")
    final val entraIdAsIdentityProvider = "entra_id"
    final val grantedAuthorities = listOf(SimpleGrantedAuthority("AUTHENTICATED_NAIS_SERVICE"))

    data class EntraAuthenticationResponse(val active: Boolean, val error: String?, val roles: List<String>?)

    override fun introspect(bearer: String?): OAuth2AuthenticatedPrincipal? {

        if (clusterProvider() == Cluster.LOCAL) {
            log.error("Authentication being treated as running locally")
            return DefaultOAuth2AuthenticatedPrincipal(mapOf<String, String>("key" to "value"), grantedAuthorities)
        }

        if (tokenIntrospectionEndpoint.isNullOrBlank()) {
            log.error("Token introspection endpoint environment variable is missing")
            throw OAuth2IntrospectionException("Token introspection endpoint environment variable is missing")
        }

        val requestBody = mapOf("identity_provider" to entraIdAsIdentityProvider, "token" to bearer)
        val authenticationResponse: EntraAuthenticationResponse = restClient.post()
            .uri(tokenIntrospectionEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(mapper.writeValueAsString(requestBody))
            .retrieve()
            .body<EntraAuthenticationResponse>()
            ?: run {
                log.warn("Token introspection endpoint returned an empty body")
                throw OAuth2IntrospectionException("Token introspection endpoint returned an empty body")
            }

        return if (!authenticationResponse.active) {
            log.debug("Invalid token received, cause for invalid token is ${authenticationResponse.error}")
            throw BadOpaqueTokenException("Invalid token received, cause for invalid token is ${authenticationResponse.error}")
        } else {
            val authenticationResponseAsMap = mapper.convertValue(authenticationResponse, object : com.fasterxml.jackson.core.type.TypeReference<Map<String, Any?>>() {})
            DefaultOAuth2AuthenticatedPrincipal(authenticationResponseAsMap, grantedAuthorities)
        }
    }
}