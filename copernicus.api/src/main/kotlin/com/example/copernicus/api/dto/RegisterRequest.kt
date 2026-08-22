package com.example.copernicus.api.dto

data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String,
    val accessLevel: String,
    val organizationId: Long? = null
) {
}