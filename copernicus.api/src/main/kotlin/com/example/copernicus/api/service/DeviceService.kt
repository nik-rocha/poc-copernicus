package com.example.copernicus.api.service

import com.example.copernicus.api.model.Collaborator
import com.example.copernicus.api.model.Device
import com.example.copernicus.api.repository.CollaboratorRepository
import com.example.copernicus.api.repository.DeviceRepository
import com.example.copernicus.api.repository.OrganizationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.nio.file.AccessDeniedException
import kotlin.math.log

@Service
class DeviceService(
    private val repository: DeviceRepository,
    private val organizationRepository: OrganizationRepository,
    private val collaboratorRepository: CollaboratorRepository
) {

    private fun getLoggedCollaborator(): Collaborator {
        val email = SecurityContextHolder.getContext().authentication?.name
        val loggedUser = collaboratorRepository.findByEmail(email)
            ?: throw RuntimeException("Usuário logado não encontrado.")

        if (loggedUser.organization == null) {
            throw AccessDeniedException("O usuário ainda não possui uma organização. Não é possível continuar.")
        }

        return loggedUser
    }

    fun create(device: Device): Device {
        val loggedUser = getLoggedCollaborator()

        device.organization = loggedUser.organization!!

        return repository.save(device)
    }

    fun findAll(): List<Device> {
        val loggedUser = getLoggedCollaborator()

        return if (loggedUser.accessLevel == "MANAGER") {
            repository.findAll()
        } else {
            val userOrgId = loggedUser.organization!!.idOrganization!!
            repository.findByOrganizationIdOrganization(userOrgId)
        }
    }

    fun update(id: Long, device: Device): Device {
        val loggedUser = getLoggedCollaborator()
        val deviceDB = repository.findByIdOrNull(id)
            ?: throw RuntimeException("O dispositivo não foi encontrado: $id.")

        if (loggedUser.accessLevel == "OPERATOR" &&
            deviceDB.organization?.idOrganization != loggedUser.organization!!.idOrganization) {
            throw AccessDeniedException("Você só pode atualizar dispositivos da sua própria organização.")
        }

        val updatedOrg = device.organization?.idOrganization?.let { orgId ->
            organizationRepository.findByIdOrNull(orgId)
                ?: throw RuntimeException("Organização não encontrada.")
        } ?: device.organization

        return repository.save(
            deviceDB.copy(
                model = device.model,
                assetTag = device.assetTag,
                organization = updatedOrg
            )
        )
    }

    fun findById(id: Long): Device {
        val loggedUser = getLoggedCollaborator()
        val device = repository.findByIdOrNull(id)
            ?: throw RuntimeException("Dispositivo com ID $id não encontrado.")

        if (loggedUser.accessLevel == "OPERATOR" &&
            device.organization?.idOrganization != loggedUser.organization!!.idOrganization) {
            throw AccessDeniedException("Você não tem permissão para visualizar este dispositivo.")
        }

        return device
    }

    fun delete(id: Long) {
        val loggedUser = getLoggedCollaborator()
        val deviceDB = repository.findByIdOrNull(id)
            ?: throw RuntimeException("O dispositivo não foi encontrado: $id.")

        if (loggedUser.accessLevel == "OPERATOR" &&
            deviceDB.organization?.idOrganization != loggedUser.organization!!.idOrganization) {
            throw AccessDeniedException("Um operador pode remover apenas dispositivos da sua própria organização.")
        }

        repository.delete(deviceDB)
    }
}