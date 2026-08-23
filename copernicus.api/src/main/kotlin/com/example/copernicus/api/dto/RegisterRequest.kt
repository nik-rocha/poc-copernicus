package com.example.copernicus.api.dto

data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String,
    val accessLevel: String,
    val organizationId: Long? = null,
    val corporateName: String? = null,
    val registrationCode: String? = null,
    val hasOrganization: Boolean? = false
) {
}