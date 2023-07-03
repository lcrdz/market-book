package com.marketbook.controller.response

data class PageResponse<T>(
    val items: List<T>,
    val currentPage: Int,
    val totalItems: Long,
    val totalPages: Int
)
