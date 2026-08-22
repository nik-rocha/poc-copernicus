package com.example.copernicus.api.service

import com.example.copernicus.api.model.Device
import com.example.copernicus.api.repository.DeviceRepository
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

    fun delete(id: Long) {
        val deviceDB = repository.findById(id)
            .orElseThrow { RuntimeException("O dispositivo não foi encontrado: $id.") }

        repository.delete(deviceDB)
    }
}