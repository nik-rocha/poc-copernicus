package com.example.copernicus.api.repository

import com.example.copernicus.api.model.Organization
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface OrganizationRepository : JpaRepository<Organization, Long> {
}