package com.marketbook.repository

import com.marketbook.helper.buildCustomer
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CustomerRepositoryTest {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @BeforeEach
    fun setup() = customerRepository.deleteAll()

    @Test
    fun `when find customer by name should return customers matches`() {
        val marcos = customerRepository.save(buildCustomer(name = "Marcos"))
        val mateus = customerRepository.save(buildCustomer(name = "Mateus"))
        customerRepository.save(buildCustomer(name = "Alex"))

        val customers = customerRepository.findByNameContaining("Ma")

        assertEquals(listOf(marcos, mateus), customers)

    }

    @Test
    fun `when email exist should return true`() {
        val email = "email@test.com"
        customerRepository.save(buildCustomer(email = email))

        val exists = customerRepository.existsByEmail(email)

        assertTrue(exists)
    }

    @Test
    fun `when email not exist should return false`() {
        val email = "other_email@test.com"

        val exists = customerRepository.existsByEmail(email)

        assertFalse(exists)
    }

    @Test
    fun `when email exist should return customer`() {
        val email = "email@test.com"
        val customer = customerRepository.save(buildCustomer(email = email))

        val response = customerRepository.findByEmail(email)

        assertNotNull(response)
        assertEquals(customer, response)
    }

    @Test
    fun `when email not exist should return null`() {
        val email = "other_email@test.com"

        val response = customerRepository.findByEmail(email)

        assertNull(response)
    }

}