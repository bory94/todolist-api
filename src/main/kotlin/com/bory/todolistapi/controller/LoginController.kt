package com.bory.todolistapi.controller

import com.bory.todolistapi.config.security.JwtServiceProvider
import com.bory.todolistapi.dto.JwtAuthenticationResponse
import com.bory.todolistapi.dto.LoginRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/public/api/v1")
class LoginController(
    private val authenticationManager: AuthenticationManager,
    private val jwtServiceProvider: JwtServiceProvider,
    private val userDetailsService: UserDetailsService
) {
    @PostMapping("/login")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.username,
                loginRequest.password
            )
        )

        SecurityContextHolder.getContext().authentication = authentication

        val userDetails = userDetailsService.loadUserByUsername(loginRequest.username)
        val jwt = jwtServiceProvider.generateToken(userDetails)

        return ResponseEntity.ok(JwtAuthenticationResponse(jwt))
    }
}

fun main() {
    val encoder = BCryptPasswordEncoder()
    println(encoder.encode("user!@#"))
}