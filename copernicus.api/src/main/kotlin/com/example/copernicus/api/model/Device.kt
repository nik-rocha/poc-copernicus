package com.example.copernicus.api.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

@Entity
@Table(name = "device")
data class Device(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_device")
    val idDevice: Long? = null,

    @Column(name = "model", nullable = false, length = 45)
    @field:NotBlank(message = "O modelo do dispositivo não pode ser vazio.")
    var model: String,

    @Column(name = "asset_tag", nullable = false, length = 45)
    @field:NotBlank(message = "A tag do dispositivo não pode ser vazia.")
    var assetTag: String,

    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = true)
    var organization: Organization? = null
) {
}