package com.example.copernicus.api.dto

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val type: String = "Bearer"
)