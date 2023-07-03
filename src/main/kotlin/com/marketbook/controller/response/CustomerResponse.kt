package com.marketbook.controller.response

import com.marketbook.enums.CustomerStatus

data class CustomerResponse(
    val id: Int? = null,
    val name: String,
    val email: String,
    val status: CustomerStatus
)
