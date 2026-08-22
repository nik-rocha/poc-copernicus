package com.example.copernicus.api.controller

import com.example.copernicus.api.model.Collaborator
import com.example.copernicus.api.service.CollaboratorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("collaborators")
class CollaboratorController(private val service: CollaboratorService) {

    @PostMapping
    fun create(@RequestBody collaborator: Collaborator) =
        ResponseEntity.status(HttpStatus.CREATED).body(service.create(collaborator))

    @GetMapping
    fun read() =
        ResponseEntity.ok(service.findAll())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Collaborator> {
        val collaborator = service.findById(id)
        return ResponseEntity.ok(collaborator)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody collaborator: Collaborator): ResponseEntity<Collaborator> =
        ResponseEntity.ok(service.update(id, collaborator))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }
}