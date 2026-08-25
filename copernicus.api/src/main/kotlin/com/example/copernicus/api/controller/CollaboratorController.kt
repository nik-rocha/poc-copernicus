package com.example.copernicus.api.controller

import com.example.copernicus.api.dto.CollaboratorCreateRequest
import com.example.copernicus.api.dto.CollaboratorResponse
import com.example.copernicus.api.dto.toResponse
import com.example.copernicus.api.model.Collaborator
import com.example.copernicus.api.service.CollaboratorService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("collaborators")
class CollaboratorController(private val service: CollaboratorService) {

    @PostMapping
    fun create(@Valid @RequestBody request: CollaboratorCreateRequest): ResponseEntity<CollaboratorResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(service.create(request).toResponse())

    @GetMapping
    fun read(): ResponseEntity<List<CollaboratorResponse>> =
        ResponseEntity.ok(service.findAll().map { it.toResponse() })

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<CollaboratorResponse> {
        val collaborator = service.findById(id)
        return ResponseEntity.ok(collaborator.toResponse())
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody collaborator: Collaborator): ResponseEntity<CollaboratorResponse> =
        ResponseEntity.ok(service.update(id, collaborator).toResponse())

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }
}