package com.example.copernicus.api.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CollaboratorCreateRequest(
    @field:NotBlank(message = "O nome do colaborador não pode estar vazio.")
    val fullName: String,
    @field:NotBlank(message = "O e-mail do colaborador não pode estar vazio.")
    val email: String,
    @field:NotBlank(message = "A senha do colaborador não pode estar vazia.")
    val password: String,
    @field:NotBlank(message = "O nível de acesso do colaborador não pode estar vazio.")
    val accessLevel: String,
    @field:NotNull(message = "Preencha todos os campos de organizaçao.")
    @field:Min(value = 1, message = "Selecione uma organização válida.")
    val organizationId: Long?
)