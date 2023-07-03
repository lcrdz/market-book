package com.marketbook.helper

import com.marketbook.enums.CustomerStatus
import com.marketbook.enums.Role
import com.marketbook.model.Book
import com.marketbook.model.Customer
import com.marketbook.model.Purchase
import java.util.*

fun buildCustomer(
    id: Int? = null,
    name: String = "Customer Name",
    email: String = "customer_${UUID.randomUUID()}@email.com",
    password: String = "password",
    isAdmin: Boolean = false
) = Customer(
    id = id,
    name = name,
    email = email,
    password = password,
    status = CustomerStatus.ACTIVE,
    roles = if(isAdmin) setOf(Role.CUSTOMER, Role.ADMIN) else setOf(Role.CUSTOMER)
)

fun buildCustomerList(quantity: Int): List<Customer> {
    val customers: MutableList<Customer> = mutableListOf()
    repeat(quantity) {
        customers.add(buildCustomer())
    }
    return customers
}

fun buildPurchase(
    id: Int? = null,
    customer: Customer = buildCustomer(),
    books: MutableList<Book> = mutableListOf(),
    nfe: String? = UUID.randomUUID().toString(),
    price: Double = 19.99
) = Purchase(
    id = id,
    customer = customer,
    books = books,
    nfe = nfe,
    price = price
)