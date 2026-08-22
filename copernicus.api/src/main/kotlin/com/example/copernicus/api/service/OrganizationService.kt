package com.example.copernicus.api.service

import com.example.copernicus.api.model.Device
import com.example.copernicus.api.model.Organization
import com.example.copernicus.api.repository.OrganizationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class OrganizationService(private val repository: OrganizationRepository) {

    fun create(organization: Organization): Organization {
        return repository.save(organization)
    }

    fun findAll(): List<Organization> {
        return repository.findAll()
    }

    fun update(id: Long, organization: Organization): Organization {
        val organizationDB = repository.findById(id)
            .orElseThrow { RuntimeException("A organização não foi encontrada: $id.") }

        return repository.save(
            organizationDB.copy(
                corporateName = organization.corporateName,
                registrationCode = organization.registrationCode
            )
        )
    }

    fun findById(id: Long): Organization {
        return repository.findByIdOrNull(id)
            ?: throw RuntimeException("Organização com ID $id não encontrado.")
    }

    fun delete(id: Long) {
        val organizationDB = repository.findById(id)
            .orElseThrow { RuntimeException("A organização não foi encontrada: $id.") }

        repository.delete(organizationDB)
    }
}