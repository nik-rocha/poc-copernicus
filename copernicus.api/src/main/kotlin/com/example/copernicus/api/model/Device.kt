package com.example.copernicus.api.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "device")
data class Device(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_device")
    val idDevice: Long? = null,

    @Column(name = "model", nullable = false, length = 45)
    var model: String,

    @Column(name = "asset_tag", nullable = false, length = 45)
    var assetTag: String,

    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    var organization: Organization
) {
}