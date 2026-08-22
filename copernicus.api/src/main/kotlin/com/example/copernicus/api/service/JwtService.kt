package com.example.copernicus.api.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date

@Service
class JwtService(
    @Value("\${jwt.secret}")
    private val secret: String,

    @Value("\${jwt.expiration-ms}")
    private val expirationMs: Long
) {

    private val secretKey = Keys.hmacShaKeyFor(secret.toByteArray())

    fun generateToken(email: String): String {
        val now = Date()
        val expiryDate = Date(now.time + expirationMs)

        return Jwts.builder()
            .subject(email)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey)
            .compact()
    }

    fun getEmailFromToken(token: String): String {
        return getClaims(token).subject
    }

    fun isTokenValid(token: String): Boolean {
        return runCatching {
            val claims = getClaims(token)
            !claims.expiration.before(Date())
        }.getOrDefault(false)
    }

    private fun getClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload as Claims
    }
}