package com.marketbook.service

import com.marketbook.events.PurchaseEvent
import com.marketbook.helper.buildPurchase
import com.marketbook.repository.PurchaseRepository
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationEventPublisher


@ExtendWith(MockKExtension::class)
internal class PurchaseServiceTest {

    @InjectMockKs
    private lateinit var purchaseService: PurchaseService

    @MockK
    private lateinit var repository: PurchaseRepository

    @MockK
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    private val purchaseEventSlot = slot<PurchaseEvent>()

    @Test
    fun `when create purchase should publish event`() {
        val purchase = buildPurchase()

        every { repository.save(purchase) } returns purchase
        every { applicationEventPublisher.publishEvent(any()) } just runs

        purchaseService.create(purchase)

        verify(exactly = 1) { repository.save(purchase) }
        verify(exactly = 1) { applicationEventPublisher.publishEvent(capture(purchaseEventSlot)) }

        assertEquals(purchase, purchaseEventSlot.captured.purchase)
    }

    @Test
    fun `when update purchase save new purchase data`() {
        val purchase = buildPurchase()

        every { repository.save(purchase) } returns purchase

        purchaseService.update(purchase)

        verify(exactly = 1) { repository.save(purchase) }
    }

}