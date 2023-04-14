package com.bory.todolistapi.config.security

import com.bory.todolistapi.exception.InvalidJwtTokenException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

private const val BEARER_KEY = "Bearer "
private const val BEARER_KEY_LENGTH = BEARER_KEY.length

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtServiceProvider,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {
    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)
    }
    
    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.authorizationHeader()
        if (token == null) {
            filterChain.doFilter(request, response)
            return
        }

        val email = jwtService.extractSubject(token)
        if (email != null && SecurityContextHolder.getContext().authentication != null) {
            filterChain.doFilter(request, response)
            return
        }
        if (email == null) {
            filterChain.doFilter(request, response)
            return
        }

        loadUserDetailsAndSetSecurityContext(email, token, request)

        filterChain.doFilter(request, response)
    }

    private fun loadUserDetailsAndSetSecurityContext(
        email: String,
        token: String,
        request: HttpServletRequest
    ) {
        try {
            val foundUser = userDetailsService.loadUserByUsername(email)
            if (!jwtService.isTokenValid(token, foundUser)) {
                throw InvalidJwtTokenException("Token's email and User email not match")
            }
            val authentication =
                UsernamePasswordAuthenticationToken(foundUser, null, foundUser.authorities)
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authentication
        } catch (rne: UsernameNotFoundException) {
            LOGGER.error("No User loaded by Jwt.", rne)
        } catch (ijte: InvalidJwtTokenException) {
            LOGGER.error("Jwt Token is not valid.", ijte)
        }
    }
}

private fun HttpServletRequest.authorizationHeader(): String? {
    val authHeader = getHeader(HttpHeaders.AUTHORIZATION) ?: ""
    if (!authHeader.startsWith(BEARER_KEY)) {
        return null
    }

    return authHeader.substring(BEARER_KEY_LENGTH)
}