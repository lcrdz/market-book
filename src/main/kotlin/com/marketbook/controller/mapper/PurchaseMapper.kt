package com.marketbook.controller.mapper

import com.marketbook.controller.request.PurchaseRequest
import com.marketbook.enums.BookStatus
import com.marketbook.enums.Errors
import com.marketbook.exception.BadRequestException
import com.marketbook.model.Purchase
import com.marketbook.service.BookService
import com.marketbook.service.CustomerService
import org.springframework.stereotype.Component

@Component
class PurchaseMapper(
    private val bookService: BookService,
    private val customerService: CustomerService
) {

    fun toPurchase(request: PurchaseRequest): Purchase {
        val customer = customerService.findById(request.customerId)
        val books = bookService.findAllById(request.bookIds)

        books.forEach {
            if (it.status == BookStatus.SOLD || it.status == BookStatus.DELETED) {
                throw throw BadRequestException(Errors.MB103.message.format(it.status), Errors.MB103.code)
            }
        }

        return Purchase(
            customer = customer,
            books = books.toMutableList(),
            price = books.sumOf { it.price }
        )
    }
}