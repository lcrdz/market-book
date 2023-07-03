package com.marketbook.security

import com.marketbook.exception.AuthenticationException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil {

    @Value("\${jwt.expiration}")
    private val expiration: Long? = null

    @Value("\${jwt.secret}")
    private val secret: String? = null

    fun generateToken(id: Int): String = Jwts.builder()
        .setSubject(id.toString())
        .setExpiration(Date(System.currentTimeMillis().plus(expiration!!)))
        .signWith(SignatureAlgorithm.HS512, secret!!.toByteArray())
        .compact()

    fun isValidToken(token: String): Boolean {
        val claims = getClaims(token)
        return (claims.subject == null || claims.expiration == null || Date().after(claims.expiration)).not()
    }

    private fun getClaims(token: String): Claims {
        try {
            return Jwts.parser().setSigningKey(secret!!.toByteArray()).parseClaimsJws(token).body
        } catch (e: Exception) {
            throw AuthenticationException("Invalid token", "999")
        }
    }

    fun getSubject(token: String): String = getClaims(token).subject
}