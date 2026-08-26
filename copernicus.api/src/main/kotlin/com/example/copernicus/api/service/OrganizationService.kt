package com.example.copernicus.api.service

import com.example.copernicus.api.exception.ConflictException
import com.example.copernicus.api.exception.ForbiddenActionException
import com.example.copernicus.api.exception.ResourceNotFoundException
import com.example.copernicus.api.model.Collaborator
import com.example.copernicus.api.model.Organization
import com.example.copernicus.api.repository.CollaboratorRepository
import com.example.copernicus.api.repository.OrganizationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class OrganizationService(
    private val repository: OrganizationRepository,
    private val collaboratorRepository: CollaboratorRepository
) {

    private fun getLoggedCollaborator(): Collaborator {
        val email = SecurityContextHolder.getContext().authentication?.name
        val loggedUser = collaboratorRepository.findByEmail(email)
            ?: throw ResourceNotFoundException("Usuário logado não encontrado.")

        if (loggedUser.organization == null) {
            throw ForbiddenActionException("Você precisa criar uma organização antes de acessar este recurso.")
        }

        return loggedUser
    }

    fun create(organization: Organization): Organization {
        val email = SecurityContextHolder.getContext().authentication?.name
        val loggedUser = collaboratorRepository.findByEmail(email)
            ?: throw ResourceNotFoundException("Usuário logado não encontrado.")

        if (loggedUser.organization != null && loggedUser.accessLevel == "OPERATOR") {
            throw ForbiddenActionException("Operadores não podem criar outras organizações.")
        }

        if (repository.findByRegistrationCode(organization.registrationCode) != null) {
            throw ConflictException("CNPJ já cadastrado no sistema.")
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

        return if (loggedUser.accessLevel.equals("MANAGER", ignoreCase = true)) {
            repository.findAll()
        } else {
            val org = loggedUser.organization
                ?: throw ForbiddenActionException("Você não possui uma organização vinculada.")
            listOf(org)
        }
    }

    fun findAllOnlogin(): List<Organization> {
        return repository.findAll()
    }

    fun update(id: Long, organization: Organization): Organization {
        val loggedUser = getLoggedCollaborator()
        val organizationDB = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("A organização não foi encontrada: $id.") }

        if (loggedUser.accessLevel.equals("OPERATOR", ignoreCase = true)) {
            throw ForbiddenActionException("Operadores não podem editar outras organizações.")
        }

        val codeOwner = repository.findByRegistrationCode(organization.registrationCode)
        if (codeOwner != null && codeOwner.idOrganization != id) {
            throw ConflictException("CNPJ já cadastrado no sistema.")
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
            ?: throw ResourceNotFoundException("Organização com ID $id não encontrada.")

        if (loggedUser.accessLevel == "OPERATOR" && loggedUser.organization?.idOrganization != id) {
            throw ForbiddenActionException("Você só pode visualizar a sua própria organização.")
        }

        return organization
    }

    fun delete(id: Long) {
        val loggedUser = getLoggedCollaborator()
        val organizationDB = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("A organização não foi encontrada: $id.") }

        if (loggedUser.accessLevel.equals("OPERATOR", ignoreCase = true)) {
            throw ForbiddenActionException("Operadores não podem remover outras organizações.")
        }

        repository.delete(organizationDB)
    }
}