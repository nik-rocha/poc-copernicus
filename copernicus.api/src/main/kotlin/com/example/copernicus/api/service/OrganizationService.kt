package com.example.copernicus.api.service

import com.example.copernicus.api.model.Collaborator
import com.example.copernicus.api.model.Device
import com.example.copernicus.api.model.Organization
import com.example.copernicus.api.repository.CollaboratorRepository
import com.example.copernicus.api.repository.OrganizationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import java.nio.file.AccessDeniedException
import org.springframework.stereotype.Service

@Service
class OrganizationService(
    private val repository: OrganizationRepository,
    private val collaboratorRepository: CollaboratorRepository
) {

    private fun getLoggedCollaborator(): Collaborator {
        val email = SecurityContextHolder.getContext().authentication?.name
        val loggedUser = collaboratorRepository.findByEmail(email)
            ?: throw RuntimeException("Usuário logado não encontrado.")

        if (loggedUser.organization == null) {
            throw AccessDeniedException("Você precisa criar uma organização antes de acessar este recurso.")
        }

        return loggedUser
        }

    fun create(organization: Organization): Organization {
        val email = SecurityContextHolder.getContext().authentication?.name
        val loggedUser = collaboratorRepository.findByEmail(email)
            ?: throw RuntimeException("Usuário logado não encontrado.")

        if (loggedUser.organization != null && loggedUser.accessLevel == "OPERATOR") {
            throw AccessDeniedException("Operadores não podem criar outras organizações.")
        }

        val newOrganization = repository.save(
            Organization(
                corporateName = organization.corporateName,
                registrationCode = organization.registrationCode
            )
        )

        if (loggedUser.organization == null) {
            loggedUser.organization = newOrganization
            collaboratorRepository.save(loggedUser)
        }

        return newOrganization
    }

    fun findAll(): List<Organization> {
        val loggedUser = getLoggedCollaborator()

        return if (loggedUser.accessLevel == "MANAGER") {
            repository.findAll()
        } else {
            listOf(loggedUser.organization!!)
        }
    }

    fun update(id: Long, organization: Organization): Organization {
        val loggedUser = getLoggedCollaborator()
        val organizationDB = repository.findById(id)
            .orElseThrow { RuntimeException("A organização não foi encontrada: $id.") }

        if (loggedUser.accessLevel.equals("OPERATOR", ignoreCase = true)) {
            throw AccessDeniedException("Operadores não podem editar outras organizações.")
        }

        return repository.save(
            organizationDB.copy(
                corporateName = organization.corporateName,
                registrationCode = organization.registrationCode
            )
        )
    }

    fun findById(id: Long): Organization {
        val loggedUser = getLoggedCollaborator()
        val organization = repository.findByIdOrNull(id)
            ?: throw RuntimeException("Organização com ID $id não encontrada.")

        if (loggedUser.accessLevel == "OPERATOR" && loggedUser.organization?.idOrganization != id) {
            throw AccessDeniedException("Você só pode visualizar a sua própria organização.")
        }

        return organization
    }

    fun delete(id: Long) {
        val loggedUser = getLoggedCollaborator()
        val organizationDB = repository.findById(id)
            .orElseThrow { RuntimeException("A organização não foi encontrada: $id.") }

        if (loggedUser.accessLevel.equals("OPERATOR", ignoreCase = true)) {
            throw AccessDeniedException("Operadores não podem remover outras organizações.")
        }

        repository.delete(organizationDB)
    }
}