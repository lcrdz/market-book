package com.marketbook.events

import com.marketbook.model.Purchase
import org.springframework.context.ApplicationEvent

class PurchaseEvent(
    source: Any,
    val purchase: Purchase
) : ApplicationEvent(source) {

}
