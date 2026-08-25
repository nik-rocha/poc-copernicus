package com.example.copernicus.api.controller

import com.example.copernicus.api.dto.DeviceCreateRequest
import com.example.copernicus.api.dto.DeviceResponse
import com.example.copernicus.api.dto.toResponse
import com.example.copernicus.api.model.Collaborator
import com.example.copernicus.api.model.Device
import com.example.copernicus.api.service.DeviceService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("devices")
class DeviceController(private val service: DeviceService) {

    @PostMapping
    fun create(@RequestBody request: DeviceCreateRequest): ResponseEntity<DeviceResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(service.create(request).toResponse())

    @GetMapping
    fun read() =
        ResponseEntity.ok(service.findAll().map { it.toResponse() })

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<DeviceResponse> =
        ResponseEntity.ok(service.findById(id).toResponse())

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody device: Device): ResponseEntity<DeviceResponse> =
        ResponseEntity.ok(service.update(id, device).toResponse())

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }
}