package com.example.copernicus.api.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "organization")
data class Organization(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_organization")
    val idOrganization: Long? = null,

    @Column(name = "corporate_name", nullable = false, length = 45)
    val corporateName: String,

    @Column(name = "registration_code", nullable = false, unique = true, length = 150)
    val registrationCode: String,

    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
}