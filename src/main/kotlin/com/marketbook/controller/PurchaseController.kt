package com.marketbook.controller

import com.marketbook.controller.mapper.PurchaseMapper
import com.marketbook.controller.request.PurchaseRequest
import com.marketbook.controller.response.PurchaseResponse
import com.marketbook.extension.toResponse
import com.marketbook.service.PurchaseService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("purchases")
class PurchaseController(
    private val service: PurchaseService,
    private val mapper: PurchaseMapper
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun purchase(@RequestBody purchase: PurchaseRequest) {
        service.create(mapper.toPurchase(purchase))
    }

    @GetMapping("/{id}")
    fun findByCustomer(@PathVariable id: Int): List<PurchaseResponse> =
        service.findByCustomer(id).map { it.toResponse() }
}