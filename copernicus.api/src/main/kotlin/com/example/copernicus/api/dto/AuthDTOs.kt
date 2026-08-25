package com.example.copernicus.api.dto

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message = "O email não pode estar vazio.")
    val email: String,
    @field:NotBlank(message = "A senha não pode estar vazia.")
    val password: String
)

data class LoginResponse(
    val token: String,
    val type: String = "Bearer"
)