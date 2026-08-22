package com.example.copernicus.api.service

import com.example.copernicus.api.model.Collaborator
import com.example.copernicus.api.model.Device
import com.example.copernicus.api.repository.DeviceRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class DeviceService(private val repository: DeviceRepository) {

    fun create(device: Device): Device {
        return repository.save(device)
    }

    fun findAll(): List<Device> {
        return repository.findAll()
    }

    fun update(id: Long, device: Device): Device {
        val deviceDB = repository.findById(id)
            .orElseThrow { RuntimeException("O dispositivo não foi encontrado: $id.") }

        return repository.save(
            deviceDB.copy(
                model = device.model,
                assetTag = device.assetTag
            )
        )
    }

    fun findById(id: Long): Device {
        return repository.findByIdOrNull(id)
            ?: throw RuntimeException("Dispositivo com ID $id não encontrado.")
    }

    fun delete(id: Long) {
        val deviceDB = repository.findById(id)
            .orElseThrow { RuntimeException("O dispositivo não foi encontrado: $id.") }

        repository.delete(deviceDB)
    }

    fun listDevices(authenticatedCollaborator: Collaborator): List<Device> {
        return if (authenticatedCollaborator.accessLevel == "MANAGER") {
            repository.findAll()
        } else {
            val orgId = authenticatedCollaborator.organization.idOrganization
                ?: throw IllegalArgumentException("A organização do colaborador não foi definida.")

            repository.findByOrganizationIdOrganization(orgId)
        }
    }
}