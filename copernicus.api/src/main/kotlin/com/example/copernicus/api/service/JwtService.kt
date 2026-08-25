package com.example.copernicus.api.service

import com.example.copernicus.api.exception.ForbiddenActionException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
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
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: ExpiredJwtException) {
            throw ForbiddenActionException("Token JWT expirado.")
        } catch (e: SignatureException) {
            throw ForbiddenActionException("Assinatura do token JWT inválida.")
        } catch (e: MalformedJwtException) {
            throw ForbiddenActionException("Token JWT malformado.")
        } catch (e: UnsupportedJwtException) {
            throw ForbiddenActionException("Token JWT não suportado.")
        } catch (e: IllegalArgumentException) {
            throw ForbiddenActionException("Token JWT ausente ou inválido.")
        } catch (e: JwtException) {
            throw ForbiddenActionException("Falha na validação do token JWT.")
        }
    }
}