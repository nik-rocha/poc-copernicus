package com.example.copernicus.api.service

import com.example.copernicus.api.dto.RegisterRequest
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
            ?: throw RuntimeException("Usuário logado não encontrado.")

        if (loggedUser.organization == null) {
            throw AccessDeniedException("O usuário ainda não possui uma organização. Não é possível continuar.")
        }

        return loggedUser
    }

    fun create(collaborator: Collaborator): Collaborator {
        val email = SecurityContextHolder.getContext().authentication?.name
        val loggedUser = repository.findByEmail(email)
            ?: throw RuntimeException("Usuário logado não encontrado.")

        val loggedUserOrg = loggedUser.organization
            ?: throw AccessDeniedException("Você precisa possuir uma organização para cadastrar novos colaboradores.")

        if (loggedUser.accessLevel.equals("OPERATOR", ignoreCase = true)) {
            throw AccessDeniedException("Operadores não podem adicionar outros colaboradores.")
        }

        if (repository.findByEmail(collaborator.email) != null) {
            throw RuntimeException("E-mail já cadastrado no sistema.")
        }

        val updatedOrg = collaborator.organization?.idOrganization?.let { orgId ->
            organizationRepository.findByIdOrNull(orgId)
                ?: throw RuntimeException("Organização não encontrada.")
        } ?: collaborator.organization

        val newCollaborator = Collaborator(
            fullName = collaborator.fullName,
            email = collaborator.email,
            password = passwordEncoder.encode(collaborator.password),
            accessLevel = collaborator.accessLevel,
            organization = updatedOrg,
        )

        return repository.save(newCollaborator)
    }
    @Transactional
    fun register(request: RegisterRequest): Collaborator {
        if (repository.findByEmail(request.email) != null) {
            throw RuntimeException("E-mail já cadastrado no sistema.")
        }

        val organization: Organization? = when {
            request.organizationId != null -> {
                organizationRepository.findByIdOrNull(request.organizationId)
                    ?: throw RuntimeException("Organização não encontrada com ID: ${request.organizationId}")
            }

            !request.corporateName.isNullOrBlank() && !request.registrationCode.isNullOrBlank() -> {
                // Armazena o código com segurança
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
        val collaboratorDB = repository.findById(id).orElseThrow { RuntimeException("O colaborador não foi encontrado: $id.")}

        if (loggedUser.accessLevel.equals("OPERATOR", ignoreCase = true)) {
            throw AccessDeniedException("Operadores não podem editar outros colaboradores.")
        }

        return repository.save(
            collaboratorDB.copy(
                fullName = collaborator.fullName,
                email = collaborator.email,
                password = collaborator.password,
                accessLevel = collaborator.accessLevel
            )
        )
    }

    fun findById(id: Long): Collaborator {
        val loggedUser = getLoggedCollaborator()
        val collaboratorDB = repository.findByIdOrNull(id)
            ?: throw RuntimeException("O colaborador não foi encontrado: $id.")

        if (loggedUser.accessLevel == "OPERATOR" &&
            collaboratorDB.organization?.idOrganization != loggedUser.organization?.idOrganization) {
            throw AccessDeniedException("Você só pode atualizar colaboradores da sua própria organização.")
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
            ?: throw RuntimeException("O colaborador não foi encontrado: $id.")

        if (loggedUser.accessLevel.equals("OPERATOR", ignoreCase = true)) {
            throw AccessDeniedException("Operadores não podem remover outros colaboradores.")
        }

        if (loggedUser.accessLevel == "OPERATOR" &&
            collaboratorDB.organization?.idOrganization != loggedUser.organization?.idOrganization) {
            throw AccessDeniedException("Você só pode excluir colaboradores da sua própria organização.")
        }

        repository.delete(collaboratorDB)
    }
}