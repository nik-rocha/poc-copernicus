package com.example.copernicus.api.controller

import com.example.copernicus.api.model.Organization
import com.example.copernicus.api.dto.OrganizationResponse
import com.example.copernicus.api.dto.toResponse
import com.example.copernicus.api.exception.ConflictException
import com.example.copernicus.api.exception.MissingDataException
import com.example.copernicus.api.exception.ResourceNotFoundException
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
    fun create(@RequestBody organization: Organization): ResponseEntity<OrganizationResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(service.create(organization).toResponse())

    @GetMapping("/all")
    fun readPublic() =
        ResponseEntity.ok(service.findAllOnlogin().map { it.toResponse() })

    @GetMapping("/authenticated")
    fun read() =
        ResponseEntity.ok(service.findAll().map { it.toResponse() })

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<OrganizationResponse> {
        val organization = service.findById(id)
        return ResponseEntity.ok(organization.toResponse())
    }

    @GetMapping
    fun getCurrentOrganization(authentication: Authentication): ResponseEntity<OrganizationResponse> {
        val email = authentication.name
        val collaborator = collaboratorRepository.findByEmail(email)
            ?: throw ResourceNotFoundException("Colaborador não encontrado")
        val organization = collaborator.organization
            ?: throw MissingDataException("Usuário não possui organização")
        return ResponseEntity.ok(organization.toResponse())
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody organization: Organization): ResponseEntity<OrganizationResponse> =
        ResponseEntity.ok(service.update(id, organization).toResponse())

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }
}