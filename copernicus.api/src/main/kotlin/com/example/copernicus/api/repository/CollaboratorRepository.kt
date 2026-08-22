package com.example.copernicus.api.repository

import com.example.copernicus.api.model.Collaborator
import org.springframework.data.jpa.repository.JpaRepository

interface CollaboratorRepository : JpaRepository<Collaborator, Long> {
    fun findByEmail(email: String?): Collaborator?
    fun findByOrganizationIdOrganization(organizationId: Long): List<Collaborator>
}