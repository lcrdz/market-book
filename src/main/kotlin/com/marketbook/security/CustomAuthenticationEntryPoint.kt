package com.marketbook.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.marketbook.controller.response.ErrorResponse
import com.marketbook.enums.Errors
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?, response: HttpServletResponse, authException: AuthenticationException?
    ) {
        response.apply {
            contentType = "application/json"
            status = HttpServletResponse.SC_UNAUTHORIZED
        }
        val errorResponse = ErrorResponse(HttpStatus.UNAUTHORIZED.value(), Errors.MB000.message, Errors.MB000.code, null)
        response.outputStream.print(jacksonObjectMapper().writeValueAsString(errorResponse))
    }
}