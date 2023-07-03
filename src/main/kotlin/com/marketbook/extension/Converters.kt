package com.marketbook.extension

import com.marketbook.controller.request.BookRequest
import com.marketbook.controller.request.CustomerRequest
import com.marketbook.controller.request.UpdateBookRequest
import com.marketbook.controller.request.UpdateCustomerRequest
import com.marketbook.controller.response.BookResponse
import com.marketbook.controller.response.CustomerResponse
import com.marketbook.controller.response.PageResponse
import com.marketbook.controller.response.PurchaseResponse
import com.marketbook.enums.BookStatus
import com.marketbook.enums.CustomerStatus
import com.marketbook.model.Book
import com.marketbook.model.Customer
import com.marketbook.model.Purchase
import org.springframework.data.domain.Page

fun CustomerRequest.toCustomer(): Customer = Customer(
    name = this.name,
    email = this.email,
    status = CustomerStatus.ACTIVE,
    password = this.password
)

fun UpdateCustomerRequest.toCustomer(oldValue: Customer): Customer = Customer(
    id = oldValue.id,
    name = this.name ?: oldValue.name,
    email = this.email ?: oldValue.email,
    status = oldValue.status,
    password = oldValue.password
)

fun BookRequest.toBook(customer: Customer): Book = Book(
    name = this.name,
    price = this.price,
    status = BookStatus.ACTIVE,
    customer = customer
)

fun UpdateBookRequest.toBook(oldValue: Book): Book = Book(
    id = oldValue.id,
    name = this.name ?: oldValue.name,
    price = this.price ?: oldValue.price,
    status = oldValue.status,
    customer = oldValue.customer
)

fun Customer.toResponse(): CustomerResponse = CustomerResponse(
    id = this.id,
    name = this.name,
    email = this.email,
    status = this.status
)

fun Book.toResponse(): BookResponse = BookResponse(
    id = this.id,
    name = this.name,
    price = this.price,
    status = this.status,
    customer = this.customer
)

fun Purchase.toResponse(): PurchaseResponse = PurchaseResponse(
    id = this.id,
    customer = this.customer,
    books = this.books.map { it.toResponse() },
    nfe = this.nfe,
    price = this.price,
    createdAt = this.createdAt
)

fun <T> Page<T>.toPageResponse(): PageResponse<T> {
    return PageResponse(
        items = this.content.toList(),
        currentPage = this.number,
        totalItems = this.totalElements,
        totalPages = this.totalPages
    )
}