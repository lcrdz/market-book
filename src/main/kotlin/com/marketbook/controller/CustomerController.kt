package com.marketbook.controller

import com.marketbook.controller.request.CustomerRequest
import com.marketbook.controller.request.UpdateCustomerRequest
import com.marketbook.controller.response.CustomerResponse
import com.marketbook.extension.toCustomer
import com.marketbook.extension.toResponse
import com.marketbook.security.UserAccessResource
import com.marketbook.service.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("customers")
class CustomerController(
    private val service: CustomerService
) {

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun getAll(@RequestParam name: String?): List<CustomerResponse> {
        return service.findAll(name).map { it.toResponse() }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid customer: CustomerRequest) {
        service.create(customer.toCustomer())
    }

    @GetMapping("/{id}")
    @UserAccessResource
    fun getCustomer(@PathVariable id: Int) = service.findById(id).toResponse()

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @UserAccessResource
    fun update(@PathVariable id: Int, @RequestBody @Valid customer: UpdateCustomerRequest) {
        val customerSaved = service.findById(id)
        service.update(customer.toCustomer(customerSaved))
    }

    @DeleteMapping("/{id}")
    @UserAccessResource
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Int) {
        service.delete(id)
    }
}