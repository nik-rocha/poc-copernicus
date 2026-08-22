package com.example.copernicus.api.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "collaborator")
data class Collaborator(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_collaborator")
    val idCollaborator: Long? = null,

    @Column(name = "full_name", nullable = false, length = 100)
    var fullName: String,

    @Column(name = "email", nullable = false, length = 45)
    var email: String,

    @Column(name = "password", nullable = false, length = 60)
    var password: String,

    @Column(name = "access_level", nullable = false, length = 45)
    var accessLevel: String,

    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    var organization: Organization
) {
}