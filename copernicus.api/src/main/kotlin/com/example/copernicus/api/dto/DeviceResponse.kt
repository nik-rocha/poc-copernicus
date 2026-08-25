package com.example.copernicus.api.dto

import com.example.copernicus.api.model.Device
import java.time.LocalDateTime

data class DeviceResponse(
    val idDevice: Long?,
    val model: String,
    val assetTag: String,
    val createdAt: LocalDateTime,
    val organizationId: Long?,
)

fun Device.toResponse() = DeviceResponse(
    idDevice = this.idDevice,
    model = this.model,
    assetTag = this.assetTag,
    createdAt = this.createdAt,
    organizationId = this.organization?.idOrganization
)