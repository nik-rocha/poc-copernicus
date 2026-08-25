package com.example.copernicus.api.dto

import com.example.copernicus.api.model.Organization
import java.time.LocalDateTime

data class OrganizationResponse(
    val idOrganization: Long?,
    val corporateName: String,
    val registrationCode: String,
    val createdAt: LocalDateTime,
    val collaborators: List<CollaboratorResponse>,
    val devices: List<DeviceResponse>
)

fun Organization.toResponse() = OrganizationResponse(
    idOrganization = this.idOrganization,
    corporateName = this.corporateName,
    registrationCode = this.registrationCode,
    createdAt = this.createdAt,
    collaborators = this.collaborators.map { it.toResponse() },
    devices = this.devices.map { it.toResponse() }
)