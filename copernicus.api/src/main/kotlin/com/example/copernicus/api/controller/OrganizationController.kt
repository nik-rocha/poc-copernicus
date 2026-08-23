package com.example.copernicus.api.controller

import com.example.copernicus.api.model.Collaborator
import com.example.copernicus.api.model.Organization
import com.example.copernicus.api.repository.CollaboratorRepository
import com.example.copernicus.api.service.OrganizationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("organizations")
class OrganizationController(
    private val service: OrganizationService,
    private val collaboratorRepository: CollaboratorRepository
) {

    @PostMapping
    fun create(@RequestBody organization: Organization) =
        ResponseEntity.status(HttpStatus.CREATED).body(service.create(organization))

    @GetMapping("/all")
    fun read() =
        ResponseEntity.ok(service.findAll())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Organization> {
        val organization = service.findById(id)
        return ResponseEntity.ok(organization)
    }

    @GetMapping
    fun getCurrentOrganization(authentication: Authentication): ResponseEntity<Organization> {
        val email = authentication.name

        val collaborator = collaboratorRepository.findByEmail(email)
            ?: throw RuntimeException("Colaborador não encontrado")

        val organization = collaborator.organization
            ?: throw RuntimeException("Usuário não possui organização")

        return ResponseEntity.ok(organization)
    }

        @PutMapping("/{id}")
        fun update(@PathVariable id: Long, @RequestBody organization: Organization): ResponseEntity<Organization> =
            ResponseEntity.ok(service.update(id, organization))

        @DeleteMapping("/{id}")
        fun delete(@PathVariable id: Long): ResponseEntity<Void> {
            service.delete(id)
            return ResponseEntity.noContent().build()
        }
    }