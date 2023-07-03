package com.marketbook.controller.request

import com.marketbook.validation.EmailAvailable
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class CustomerRequest(
    @field:NotEmpty(message = "O campo nome deve ser informado.")
    val name: String,
    @field:Email(message = "O campo e-mail deve ser válido.")
    @EmailAvailable
    val email: String,
    @field:NotEmpty(message = "O campo senha deve ser informado.")
    val password: String
)
