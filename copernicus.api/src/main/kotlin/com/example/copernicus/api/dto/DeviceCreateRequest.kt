package com.example.copernicus.api.dto

import jakarta.validation.constraints.NotBlank

data class DeviceCreateRequest(
    @field:NotBlank(message = "O modelo do dispositivo não pode ser vazio.")
    val model: String,
    @field:NotBlank(message = "A etiqueta do ativo do não pode ser vazia.")
    val assetTag: String,
    val organizationId: Long? = null
)