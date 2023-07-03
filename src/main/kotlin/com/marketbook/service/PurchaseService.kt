package com.marketbook.service

import com.marketbook.events.PurchaseEvent
import com.marketbook.model.Purchase
import com.marketbook.repository.PurchaseRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class PurchaseService(
    private val repository: PurchaseRepository,
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    @Transactional
    fun create(purchase: Purchase){
        repository.save(purchase)

        println("Send PurchaseEvent")
        applicationEventPublisher.publishEvent(PurchaseEvent(this, purchase))
        println("Purchase process finish!")
    }

    fun update(purchase: Purchase) {
        repository.save(purchase)
    }

    fun findByCustomer(customerId: Int): List<Purchase> =
        repository.findByCustomerId(customerId)

}
