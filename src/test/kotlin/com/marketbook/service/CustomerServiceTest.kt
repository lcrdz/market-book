package com.marketbook.service

import com.marketbook.enums.CustomerStatus
import com.marketbook.enums.Errors
import com.marketbook.exception.NotFoundException
import com.marketbook.helper.buildCustomer
import com.marketbook.helper.buildCustomerList
import com.marketbook.repository.CustomerRepository
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

@ExtendWith(MockKExtension::class)
internal class CustomerServiceTest {

    @MockK
    private lateinit var repository: CustomerRepository

    @MockK
    private lateinit var bookService: BookService

    @MockK
    private lateinit var bCrypt: BCryptPasswordEncoder

    @InjectMockKs
    @SpyK
    private lateinit var customerService: CustomerService

    @Test
    fun `when call find all without name should return all customers`() {

        val mockCustomer = buildCustomerList(4)

        every { repository.findAll() } returns mockCustomer

        val customers = customerService.findAll(null)

        assertEquals(mockCustomer.toList(), customers.toList())
        verify(exactly = 1) { repository.findAll() }
        verify(exactly = 0) { repository.findByNameContaining(any()) }
    }

    @Test
    fun `when call find all with name should return customers with searched name`() {

        val mockCustomer = listOf(
            buildCustomer(name = "Ana"),
            buildCustomer(name = "João"),
            buildCustomer(name = "Carlos"),
            buildCustomer(name = "Richard"),
            buildCustomer(name = "Renato"),
            buildCustomer(name = "Ribamar")
        )

        every { repository.findByNameContaining("name") } returns mockCustomer

        val customers = customerService.findAll("name")

        assertEquals(mockCustomer, customers)
        verify(exactly = 0) { repository.findAll() }
        verify(exactly = 1) { repository.findByNameContaining("name") }
    }

    @Test
    fun `when create customer should encrypt password`() {
        val initialPassword = "simple-password"
        val customer = buildCustomer(password = initialPassword)
        val password = UUID.randomUUID().toString()
        val customerEncrypted = customer.copy(password = password)

        every { repository.save(customerEncrypted) } returns customer
        every { bCrypt.encode(initialPassword) } returns password

        customerService.create(customer)

        verify(exactly = 1) { repository.save(customerEncrypted) }
        verify(exactly = 1) { bCrypt.encode(initialPassword) }

    }

    @Test
    fun `when find by id should return correct customer`() {
        val id = Random().nextInt()
        val customer = buildCustomer(id = id)

        every { repository.findById(id) } returns Optional.of(customer)

        val response = customerService.findById(id)

        assertEquals(customer, response)
    }

    @Test
    fun `when find by id not found customer should throw exception`() {
        val id = Random().nextInt()

        every { repository.findById(id) } returns Optional.empty()

        val error = assertThrows<NotFoundException> { customerService.findById(id) }

        assertEquals("Customer [$id] not exists", error.message)
        assertEquals("MB-201", error.errorCode)
        verify { repository.findById(id) }
    }

    @Test
    fun `when update customer should save new data`() {
        val id = Random().nextInt()
        val customer = buildCustomer(id = id)

        every { repository.existsById(id) } returns true
        every { repository.save(customer) } returns customer

        customerService.update(customer)

        verify(exactly = 1) { repository.existsById(id) }
        verify(exactly = 1) { repository.save(customer) }
    }

    @Test
    fun `when try update not found customer should throw exception`() {
        val id = Random().nextInt()
        val customer = buildCustomer(id = id)

        every { repository.existsById(id) } returns false
        every { repository.save(customer) } returns customer

        val error = assertThrows<NotFoundException> {
            customerService.update(customer)
        }

        assertEquals("Customer [$id] not exists", error.message)
        assertEquals("MB-201", error.errorCode)
        verify(exactly = 1) { repository.existsById(id) }
        verify(exactly = 0) { repository.save(customer) }
    }

    @Test
    fun `when delete customer should update status to INACTIVE`() {
        val id = Random().nextInt()
        val customer = buildCustomer(id = id)
        val expectedCustomer = customer.copy(status = CustomerStatus.INACTIVE)

        every { repository.existsById(id) } returns true
        every { customerService.findById(id) } returns customer
        every { repository.save(expectedCustomer) } returns expectedCustomer
        every { bookService.deleteByCustomer(customer) } just runs

        customerService.delete(id)

        verify(exactly = 1) { customerService.findById(any()) }
        verify(exactly = 1) { bookService.deleteByCustomer(customer) }
        verify(exactly = 1) { repository.save(expectedCustomer) }
    }

    @Test
    fun `when try delete not found customer should throw exception`() {
        val id = Random().nextInt()

        every { repository.existsById(id) } returns false
        every { customerService.findById(id) } throws NotFoundException(
            Errors.MB201.message.format(id), Errors.MB201.code
        )

        val error = assertThrows<NotFoundException> {
            customerService.delete(id)
        }

        assertEquals("Customer [$id] not exists", error.message)
        assertEquals("MB-201", error.errorCode)
        verify(exactly = 1) { customerService.findById(any()) }
        verify(exactly = 0) { bookService.deleteByCustomer(any()) }
        verify(exactly = 0) { repository.save(any()) }
    }

    @Test
    fun `when email is available should return true`() {
        val email = "name_${Random().nextInt()}@email.com"

        every { repository.existsByEmail(email) } returns false

        val emailAvailable = customerService.emailAvailable(email)

        assertTrue(emailAvailable)
        verify(exactly = 1) { repository.existsByEmail(email) }
    }

    @Test
    fun `when email is not available should return false`() {
        val email = "name_${Random().nextInt()}@email.com"

        every { repository.existsByEmail(email) } returns true

        val emailAvailable = customerService.emailAvailable(email)

        assertFalse(emailAvailable)
        verify(exactly = 1) { repository.existsByEmail(email) }
    }
}