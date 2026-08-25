package com.example.copernicus.api.dto

import com.example.copernicus.api.model.Collaborator
import java.time.LocalDateTime

data class CollaboratorResponse(
    val idCollaborator: Long?,
    val fullName: String,
    val email: String,
    val accessLevel: String,
    val createdAt: LocalDateTime,
    val organizationId: Long?,
    val organizationName: String?
)

fun Collaborator.toResponse() = CollaboratorResponse(
    idCollaborator = this.idCollaborator,
    fullName = this.fullName,
    email = this.email,
    accessLevel = this.accessLevel,
    createdAt = this.createdAt,
    organizationId = this.organization?.idOrganization,
    organizationName = this.organization?.corporateName
)