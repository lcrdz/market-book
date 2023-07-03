package com.marketbook.controller.response

import com.marketbook.model.Customer
import java.time.LocalDateTime

data class PurchaseResponse(
    val id: Int? = null,
    val customer: Customer,
    val books: List<BookResponse>,
    val nfe: String? = null,
    val price: Double,
    val createdAt: LocalDateTime
)
