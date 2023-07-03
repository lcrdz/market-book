package com.marketbook.service

import com.marketbook.enums.BookStatus
import com.marketbook.enums.Errors
import com.marketbook.exception.NotFoundException
import com.marketbook.model.Book
import com.marketbook.model.Customer
import com.marketbook.repository.BookRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BookService(
    private val repository: BookRepository
) {

    fun create(book: Book) {
        repository.save(book)
    }

    fun findAll(pageable: Pageable): Page<Book> = repository.findAll(pageable)

    fun findActives(pageable: Pageable): Page<Book> = repository.findByStatus(status = BookStatus.ACTIVE, pageable)

    fun findById(id: Int): Book =
        repository.findById(id).orElseThrow { NotFoundException(Errors.MB101.message.format(id), Errors.MB101.code) }

    fun delete(id: Int) {
        findById(id).apply {
            status = BookStatus.CANCELED
        }.also { repository.save(it) }
    }

    fun update(book: Book) {
        repository.save(book)
    }

    fun deleteByCustomer(customer: Customer) {
        val books = repository.findByCustomer(customer)
        books.map { book ->
            book.apply { status = BookStatus.DELETED }
        }.also { repository.saveAll(it) }
    }

    fun findAllById(bookIds: Set<Int>): List<Book> {
        return repository.findAllById(bookIds).toList()
    }

    fun purchase(books: MutableList<Book>) {
        books.map { it.status = BookStatus.SOLD }
        repository.saveAll(books)
    }

}