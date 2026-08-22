package com.example.copernicus.api.controller

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
    fun create(@RequestBody device: Device) =
        ResponseEntity.status(HttpStatus.CREATED).body(service.create(device))

    @GetMapping
    fun read() =
        ResponseEntity.ok(service.findAll())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Device> {
        val device = service.findById(id)
        return ResponseEntity.ok(device)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody device: Device): ResponseEntity<Device> =
        ResponseEntity.ok(service.update(id, device))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }
}