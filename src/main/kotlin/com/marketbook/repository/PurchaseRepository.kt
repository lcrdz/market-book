package com.marketbook.repository

import com.marketbook.model.Purchase
import org.springframework.data.repository.CrudRepository

interface PurchaseRepository: CrudRepository<Purchase, Int> {
    fun findByCustomerId(customerId: Int): List<Purchase>
}
