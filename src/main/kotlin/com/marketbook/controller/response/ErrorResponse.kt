package com.marketbook.controller.response

data class ErrorResponse(
    val status: Int,
    val message: String,
    val internalCode: String,
    val errors: List<FieldErrorResponse>?
)
