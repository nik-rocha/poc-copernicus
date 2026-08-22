package com.example.copernicus.api.security

import com.example.copernicus.api.repository.CollaboratorRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service

@Service
class CollaboratorDetailsService(
    private val collaboratorRepository: CollaboratorRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val collaborator = collaboratorRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("O colaborador não foi encontrado: $username")

        return User.builder()
            .username(collaborator.email)
            .password(collaborator.password)
            .authorities(listOf(SimpleGrantedAuthority(collaborator.accessLevel)))
            .build()
    }
}