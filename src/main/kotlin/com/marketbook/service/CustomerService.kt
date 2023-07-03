package com.marketbook.service

import com.marketbook.enums.CustomerStatus
import com.marketbook.enums.Errors
import com.marketbook.enums.Role
import com.marketbook.exception.NotFoundException
import com.marketbook.model.Customer
import com.marketbook.repository.CustomerRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomerService(
    private val repository: CustomerRepository,
    private val bookService: BookService,
    private val bCrypt: BCryptPasswordEncoder
) {

    fun findAll(name: String?): List<Customer> {
        return name?.let {
            repository.findByNameContaining(it)
        } ?: repository.findAll().toList()
    }

    fun create(customer: Customer) {
        val customerCopy = customer.copy(
            roles = setOf(Role.CUSTOMER),
            password = bCrypt.encode(customer.password)
        )
        repository.save(customerCopy)
    }

    fun findById(id: Int): Customer =
        repository.findById(id).orElseThrow { NotFoundException(Errors.MB201.message.format(id), Errors.MB201.code) }

    fun update(customer: Customer) {

        if (repository.existsById(customer.id!!).not()) {
            throw NotFoundException(Errors.MB201.message.format(customer.id), Errors.MB201.code)
        }

        repository.save(customer)
    }

    fun delete(id: Int) {
        val customer = findById(id)
        bookService.deleteByCustomer(customer)
        customer.apply {
            status = CustomerStatus.INACTIVE
        }.also { update(it) }
    }

    fun emailAvailable(email: String): Boolean {
        return repository.existsByEmail(email).not()
    }
}