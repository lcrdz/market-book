package com.marketbook.controller.request

data class LoginRequest(
    val email: String,
    val password: String
)