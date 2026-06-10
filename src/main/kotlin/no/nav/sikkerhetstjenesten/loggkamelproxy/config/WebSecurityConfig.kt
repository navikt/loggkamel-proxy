package no.nav.sikkerhetstjenesten.loggkamelproxy.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.sikkerhetstjenesten.loggkamelproxy.auth.NaisTokenIntrospector
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint


@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val headerAuthenticationFilter: HeaderAuthenticationFilter,
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            formLogin { disable() }
            httpBasic { disable() }
//            addFilterBefore<AnonymousAuthenticationFilter>(headerAuthenticationFilter)
            exceptionHandling {
                authenticationEntryPoint = HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
            }
            oauth2ResourceServer {
                opaqueToken {
                }
            }
            authorizeHttpRequests {
                authorize("/monitoring/**", permitAll)
                authorize(anyRequest, authenticated)
            }
        }

        return http.build()
    }

    @Bean
    fun opaqueIntrospector(environment: Environment, mapper: ObjectMapper): OpaqueTokenIntrospector {
        return NaisTokenIntrospector(environment, mapper)
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        return jacksonObjectMapper()
    }

}