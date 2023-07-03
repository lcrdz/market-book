package com.marketbook.repository

import com.marketbook.enums.BookStatus
import com.marketbook.model.Book
import com.marketbook.model.Customer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

interface BookRepository : JpaRepository<Book, Int> {

    fun findByStatus(status: BookStatus, pageable: Pageable): Page<Book>
    fun findByCustomer(customer: Customer): List<Book>
//    fun findAll(pageable: Pageable): Page<Book>
}