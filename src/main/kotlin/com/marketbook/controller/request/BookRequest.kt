package com.marketbook.controller.request

import com.fasterxml.jackson.annotation.JsonAlias
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class BookRequest(
    @field:NotEmpty(message = "O campo nome deve ser informado.")
    val name: String,
    @field:NotNull(message = "O campo preço deve ser informado.")
    val price: Double,
    @JsonAlias("customer_id")
    val customerId: Int
)
