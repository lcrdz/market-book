package com.marketbook.controller

import com.marketbook.controller.request.BookRequest
import com.marketbook.controller.request.UpdateBookRequest
import com.marketbook.controller.response.BookResponse
import com.marketbook.controller.response.PageResponse
import com.marketbook.extension.toBook
import com.marketbook.extension.toPageResponse
import com.marketbook.extension.toResponse
import com.marketbook.service.BookService
import com.marketbook.service.CustomerService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("books")
class BookController(
    private val bookService: BookService,
    private val customerService: CustomerService,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid book: BookRequest) {
        val customer = customerService.findById(book.customerId)
        bookService.create(book.toBook(customer))
    }

    @GetMapping
    fun findAll(@PageableDefault(page = 0, size = 10) pageable: Pageable): PageResponse<BookResponse> =
        bookService.findAll(pageable).map { it.toResponse() }.toPageResponse()

    @GetMapping("/active")
    fun findActives(@PageableDefault(page = 0, size = 10) pageable: Pageable): PageResponse<BookResponse> =
        bookService.findActives(pageable).map { it.toResponse() }.toPageResponse()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): BookResponse = bookService.findById(id).toResponse()

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Int) {
        bookService.delete(id)
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable id: Int, @RequestBody book: UpdateBookRequest) {
        val bookSaved = bookService.findById(id)
        bookService.update(book.toBook(bookSaved))
    }
}
