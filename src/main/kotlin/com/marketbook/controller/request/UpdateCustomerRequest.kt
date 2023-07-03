package com.marketbook.controller.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class UpdateCustomerRequest(
    @field:NotEmpty(message = "O campo nome deve ser informado.")
    val name: String?,
    @field:Email(message = "O campo e-mail deve ser válido.")
    val email: String?
)
