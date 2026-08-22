package com.example.copernicus.api.repository

import com.example.copernicus.api.model.Device
import org.springframework.data.jpa.repository.JpaRepository

interface DeviceRepository : JpaRepository<Device, Long> {
}