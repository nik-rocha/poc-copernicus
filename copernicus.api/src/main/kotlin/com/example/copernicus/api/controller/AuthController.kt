package com.example.copernicus.api.controller

import com.example.copernicus.api.dto.LoginRequest
import com.example.copernicus.api.dto.LoginResponse
import com.example.copernicus.api.dto.RegisterRequest
import com.example.copernicus.api.service.CollaboratorService
import com.example.copernicus.api.service.JwtService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    private val collaboratorService: CollaboratorService
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<*> {
        return try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.email, request.password)
            )
            val token = jwtService.generateToken(request.email)
            ResponseEntity.ok(mapOf("token" to token))
        } catch (e: BadCredentialsException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha inválidos.")
        }
    }

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<*> {
        return try {
            val newCollaborator = collaboratorService.register(request)
            ResponseEntity.status(HttpStatus.CREATED).body(newCollaborator)
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
    }
}