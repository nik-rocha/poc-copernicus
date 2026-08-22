package com.example.copernicus.api.service

import com.example.copernicus.api.model.Collaborator
import com.example.copernicus.api.repository.CollaboratorRepository
import org.springframework.stereotype.Service

@Service
class CollaboratorService(private val repository: CollaboratorRepository) {

    fun create(collaborator: Collaborator): Collaborator {
        return repository.save(collaborator)
    }

    fun findAll(): List<Collaborator> {
        return repository.findAll()
    }

    fun update(id: Long, collaborator: Collaborator): Collaborator {
        val collaboratorDB = repository.findById(id)
            .orElseThrow { RuntimeException("O colaborador não foi encontrado: $id.") }

        return repository.save(
            collaboratorDB.copy(
                fullName = collaborator.fullName,
                email = collaborator.email,
                password = collaborator.password,
                accessLevel = collaborator.accessLevel
            )
        )
    }

    fun delete(id: Long) {
        val collaboratorDB = repository.findById(id)
            .orElseThrow { RuntimeException("O colaborador não foi encontrado: $id.") }

        repository.delete(collaboratorDB)
    }
}