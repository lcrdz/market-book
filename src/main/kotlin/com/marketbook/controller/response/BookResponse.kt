package com.marketbook.controller.response

import com.marketbook.enums.BookStatus
import com.marketbook.model.Customer

data class BookResponse(
    val id: Int? = null,
    val name: String,
    val price: Double,
    val status: BookStatus? = null,
    val customer: Customer? = null
)
