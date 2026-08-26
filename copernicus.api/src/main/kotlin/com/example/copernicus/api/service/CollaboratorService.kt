package com.example.copernicus.api.service

import com.example.copernicus.api.dto.CollaboratorCreateRequest
import com.example.copernicus.api.dto.RegisterRequest
import com.example.copernicus.api.exception.ConflictException
import com.example.copernicus.api.exception.ForbiddenActionException
import com.example.copernicus.api.exception.ResourceNotFoundException
import com.example.copernicus.api.model.Collaborator
import com.example.copernicus.api.model.Organization
import com.example.copernicus.api.repository.CollaboratorRepository
import com.example.copernicus.api.repository.OrganizationRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.nio.file.AccessDeniedException

@Service
class CollaboratorService(
    private val repository: CollaboratorRepository,
    private val organizationRepository: OrganizationRepository,
    private val passwordEncoder: PasswordEncoder
) {

    private fun getLoggedCollaborator(): Collaborator {
        val email = SecurityContextHolder.getContext().authentication?.name
        val loggedUser = repository.findByEmail(email)
            ?: throw ResourceNotFoundException("Usuário logado não encontrado.")

        if (loggedUser.organization == null) {
            throw ForbiddenActionException("O usuário ainda não possui uma organização. Não é possível continuar.")
        }

        return loggedUser
    }

    fun create(request: CollaboratorCreateRequest): Collaborator {
        val normalizedEmail = request.email.trim().lowercase()
        val email = SecurityContextHolder.getContext().authentication?.name
        val loggedUser = repository.findByEmail(email)
            ?: throw ResourceNotFoundException("Usuário logado não encontrado.")

        if (loggedUser.accessLevel.equals("OPERATOR", ignoreCase = true)) {
            throw ForbiddenActionException("Operadores não podem adicionar outros colaboradores.")
        }

        if (repository.findByEmail(request.email) != null) {
            throw ConflictException("E-mail já cadastrado no sistema.")
        }

        val organization = organizationRepository.findByIdOrNull(request.organizationId!!)
            ?: throw ResourceNotFoundException("Organização não encontrada.")

        val newCollaborator = Collaborator(
            fullName = request.fullName,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            accessLevel = request.accessLevel,
            organization = organization,
        )

        return repository.save(newCollaborator)
    }

    @Transactional
    fun register(request: RegisterRequest): Collaborator {
        if (repository.findByEmail(request.email) != null) {
            throw ConflictException("E-mail já cadastrado no sistema.")
        }

        val organization: Organization? = when {
            request.organizationId != null -> {
                organizationRepository.findByIdOrNull(request.organizationId)
                    ?: throw ResourceNotFoundException("Organização não encontrada com ID: ${request.organizationId}")
            }

            !request.corporateName.isNullOrBlank() && !request.registrationCode.isNullOrBlank() -> {
                val code = request.registrationCode

                val existingOrg = organizationRepository.findByRegistrationCode(code)

                existingOrg ?: organizationRepository.save(
                    Organization(
                        corporateName = request.corporateName,
                        registrationCode = code
                    )
                )
            }

            else -> null
        }

        val collaborator = Collaborator(
            fullName = request.fullName,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            accessLevel = request.accessLevel,
            organization = organization
        )

        return repository.save(collaborator)
    }

    fun findAll(): List<Collaborator> {
        val loggedUser = getLoggedCollaborator()

        return if (loggedUser.accessLevel == "MANAGER") {
            repository.findAll()
        } else {
            val userOrgId = loggedUser.organization!!.idOrganization!!
            repository.findByOrganizationIdOrganization(userOrgId)
        }
    }

    fun update(id: Long, collaborator: Collaborator): Collaborator {
        val loggedUser = getLoggedCollaborator()
        val collaboratorDB =
            repository.findById(id).orElseThrow { ResourceNotFoundException("O colaborador não foi encontrado: $id.") }

        if (loggedUser.accessLevel.equals("OPERATOR", ignoreCase = true)) {
            throw ForbiddenActionException("Operadores não podem editar outros colaboradores.")
        }

        val normalizedEmail = collaborator.email.trim().lowercase()

        val emailOwner = repository.findByEmail(normalizedEmail)
        if (emailOwner != null && emailOwner.idCollaborator != id) {
            throw ConflictException("E-mail já cadastrado no sistema.")
        }

        return repository.save(
            collaboratorDB.copy(
                fullName = collaborator.fullName,
                email = collaborator.email,
                password = if (collaborator.password.isNullOrBlank()) collaboratorDB.password else collaborator.password,
                accessLevel = collaborator.accessLevel
            )
        )
    }

    fun findById(id: Long): Collaborator {
        val loggedUser = getLoggedCollaborator()
        val collaboratorDB = repository.findByIdOrNull(id)
            ?: throw ResourceNotFoundException("O colaborador não foi encontrado: $id.")

        if (loggedUser.accessLevel == "OPERATOR" &&
            collaboratorDB.organization?.idOrganization != loggedUser.organization?.idOrganization
        ) {
            throw ForbiddenActionException("Você só pode atualizar colaboradores da sua própria organização.")
        }

        return repository.save(
            collaboratorDB.copy(
                fullName = collaboratorDB.fullName,
                email = collaboratorDB.email,
                accessLevel = collaboratorDB.accessLevel
            )
        )
    }

    fun delete(id: Long) {
        val loggedUser = getLoggedCollaborator()
        val collaboratorDB = repository.findByIdOrNull(id)
            ?: throw ResourceNotFoundException("O colaborador não foi encontrado: $id.")

        if (loggedUser.accessLevel.equals("OPERATOR", ignoreCase = true)) {
            throw ForbiddenActionException("Operadores não podem remover outros colaboradores.")
        }

        if (loggedUser.accessLevel == "OPERATOR" &&
            collaboratorDB.organization?.idOrganization != loggedUser.organization?.idOrganization
        ) {
            throw ForbiddenActionException("Você só pode excluir colaboradores da sua própria organização.")
        }

        repository.delete(collaboratorDB)
    }
}