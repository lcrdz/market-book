package com.marketbook.controller.request

import com.fasterxml.jackson.annotation.JsonAlias
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class PurchaseRequest(
    @field:NotNull
    @field:Positive
    @JsonAlias("customer_id")
    val customerId: Int,
    @field:NotNull
    @JsonAlias("book_ids")
    val bookIds: Set<Int>
)
