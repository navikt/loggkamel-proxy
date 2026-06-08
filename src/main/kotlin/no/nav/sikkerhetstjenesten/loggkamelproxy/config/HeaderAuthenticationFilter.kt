package no.nav.sikkerhetstjenesten.loggkamelproxy.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class HeaderAuthenticationFilter(
    private val requestAuthenticationDecider: RequestAuthenticationDecider,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authenticationHeader = request.getHeader("Authentication")

        if (requestAuthenticationDecider.isRequestAuthenticated(authenticationHeader)) {
            val authentication = UsernamePasswordAuthenticationToken(
                "request-authenticated",
                null,
                emptyList(),
            )
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authentication
        } else {
            SecurityContextHolder.clearContext()
        }

        filterChain.doFilter(request, response)
    }
}

