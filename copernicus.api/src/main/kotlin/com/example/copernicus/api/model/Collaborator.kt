package com.example.copernicus.api.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

@Entity
@Table(name = "collaborator")
data class Collaborator(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_collaborator")
    val idCollaborator: Long? = null,

    @Column(name = "full_name", nullable = false, length = 100)
    @field:NotBlank(message = "O nome do colaborador não pode ser vazio.")
    var fullName: String,

    @Column(name = "email", nullable = false, length = 45)
    @field:NotBlank(message = "O e-mail do colaborador não pode ser vazio.")
    var email: String,

    @Column(name = "password", nullable = false, length = 60)
    @field:NotBlank(message = "A senha do colaborador não pode ser vazia.")
    var password: String?,

    @Column(name = "access_level", nullable = false, length = 45)
    @field:NotBlank(message = "O cargo do colaborador não pode ser vazio.")
    var accessLevel: String,

    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = true)
    var organization: Organization? = null
) {
}