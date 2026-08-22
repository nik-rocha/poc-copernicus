package com.example.copernicus.api.controller

import com.example.copernicus.api.model.Organization
import com.example.copernicus.api.repository.OrganizationRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("organizations")
class OrganizationController(val repository: OrganizationRepository) {

    @PostMapping
    fun create(@RequestBody organization: Organization) = ResponseEntity.status(HttpStatus.CREATED).body(repository.save(organization))

    @GetMapping
    fun read() = ResponseEntity.status(HttpStatus.OK).body(repository.findAll())

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody organization: Organization): ResponseEntity<Organization> {
        val organizationDB = repository.findById(id).orElseThrow { RuntimeException("A organização não foi encontrada: $id.") }
        val updatedOrganization = repository.save(organizationDB.copy(corporateName = organization.corporateName, registrationCode = organization.registrationCode))
        return ResponseEntity.ok(updatedOrganization)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = repository.findById(id).ifPresent { repository.delete(it) }

}