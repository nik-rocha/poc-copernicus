package com.example.copernicus.api.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

@Entity
@Table(name = "organization")
data class Organization(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_organization")
    val idOrganization: Long? = null,

    @Column(name = "corporate_name", nullable = false, length = 45)
    @field:NotBlank(message = "O nome da organização não pode ser vazio.")
    var corporateName: String = "",

    @Column(name = "registration_code", nullable = false, unique = true, length = 150)
    @field:NotBlank(message = "O código de registro da organização não pode ser vazio.")
    var registrationCode: String = "",

    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "organization", cascade = [CascadeType.ALL], orphanRemoval = true)
    var collaborators: MutableList<Collaborator> = mutableListOf(),

    @OneToMany(mappedBy = "organization", cascade = [CascadeType.ALL], orphanRemoval = true)
    var devices: MutableList<Device> = mutableListOf()
) {
}