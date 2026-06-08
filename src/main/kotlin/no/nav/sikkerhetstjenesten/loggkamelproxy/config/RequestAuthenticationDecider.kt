package no.nav.sikkerhetstjenesten.loggkamelproxy.config

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

    fun isRequestAuthenticated(
        authenticationHeader: String?
    ): Boolean {
        //TODO: only for local development, remove before merging
        println("Authentication header is: $authenticationHeader")

        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {
            log.info("Authentication attempted with missing or misformatted header")
            return false
        }

        val bearerToken = authenticationHeader.substringAfter("Bearer ")

        //TODO: pull this variable out so it's not a magic string in the code body
        val tokenIntrospectionEndpoint = environment.getProperty("NAIS_TOKEN_INTROSPECTION_ENDPOINT")
        if (tokenIntrospectionEndpoint.isNullOrBlank()) {
            log.info("NAIS_TOKEN_INTROSPECTION_ENDPOINT is missing")
            return false
        }

        //TODO: cast this as a map or json blob
        val authenticationResponse: String = restClient.post()
            .uri(tokenIntrospectionEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body("""{"identity_provider":"entra_id","token":"$bearerToken"}""")
            .retrieve()
            .body(String::class.java)
            ?: run {
                log.info("Token introspection endpoint returned an empty body")
                return false
            }

        //TODO: remove this debug logging, base approval on the "active" field of the response
        log.info("Token introspection response: {}", authenticationResponse)



        return true
    }
}

