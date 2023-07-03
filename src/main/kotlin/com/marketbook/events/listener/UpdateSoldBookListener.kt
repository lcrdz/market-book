package com.marketbook.events.listener

import com.marketbook.events.PurchaseEvent
import com.marketbook.service.BookService
import com.marketbook.service.PurchaseService
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.*

@Component
class UpdateSoldBookListener(
    private val bookService: BookService
) {

    @Async
    @EventListener
    fun listener(purchaseEvent: PurchaseEvent) {
        println("Update books status...")
        bookService.purchase(purchaseEvent.purchase.books)
    }
}