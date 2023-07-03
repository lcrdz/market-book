package com.marketbook.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.marketbook.controller.request.CustomerRequest
import com.marketbook.controller.request.UpdateCustomerRequest
import com.marketbook.enums.CustomerStatus
import com.marketbook.helper.buildCustomer
import com.marketbook.repository.CustomerRepository
import com.marketbook.security.UserCustomDetails
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.random.Random

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@ActiveProfiles("test")
@WithMockUser
internal class CustomerControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() = customerRepository.deleteAll()

    @AfterEach
    fun tearDown() = customerRepository.deleteAll()

    @Test
    fun `when get all should return all customer`() {
        val admin = customerRepository.save(buildCustomer(isAdmin = true))
        val customer = customerRepository.save(buildCustomer())

        mockMvc.perform(get("/customers").with(user(UserCustomDetails(admin))))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(admin.id))
            .andExpect(jsonPath("$[0].name").value(admin.name))
            .andExpect(jsonPath("$[0].email").value(admin.email))
            .andExpect(jsonPath("$[1].id").value(customer.id))
            .andExpect(jsonPath("$[1].name").value(customer.name))
            .andExpect(jsonPath("$[1].email").value(customer.email))
    }

    @Test
    fun `when filter customers should return matcher customer`() {
        val admin = customerRepository.save(buildCustomer(name = "Luiz", isAdmin = true))
        val customer = customerRepository.save(buildCustomer(name = "Carlos"))

        mockMvc.perform(get("/customers?name=Lu").with(user(UserCustomDetails(admin))))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value(admin.id))
            .andExpect(jsonPath("$[0].name").value(admin.name))
            .andExpect(jsonPath("$[0].email").value(admin.email))
    }

    @Test
    fun `when create customer should create with success`() {
        val request = CustomerRequest("fake name", "${Random.nextInt()}@fakemail.com", "123456")
        mockMvc.perform(
            post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)

        val customers = customerRepository.findAll().toList()
        assertEquals(1, customers.size)
        assertEquals(request.name, customers[0].name)
        assertEquals(request.email, customers[0].email)
    }

    @Test
    fun `when get user by id with different id should return forbidden`() {
        val customer = customerRepository.save(buildCustomer())

        mockMvc.perform(get("/customers/5").with(user(UserCustomDetails(customer))))
            .andExpect(status().isForbidden)
            .andExpect(jsonPath("$.status").value(403))
            .andExpect(jsonPath("$.message").value("Access Denied"))
            .andExpect(jsonPath("$.internalCode").value("MB-000"))
    }

    @Test
    fun `when update customer should override customer with new data`() {
        val customer = customerRepository.save(buildCustomer())
        val request = UpdateCustomerRequest("Luiz", "luiz@email.com")

        mockMvc.perform(
            put("/customers/${customer.id}")
                .with(user(UserCustomDetails(customer)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isNoContent)

        val customers = customerRepository.findAll().toList()
        assertEquals(1, customers.size)
        assertEquals(request.name, customers[0].name)
        assertEquals(request.email, customers[0].email)
    }

    @Test
    fun `when try create customer with invalid information should throw error`() {
        val request = CustomerRequest("", "${Random.nextInt()}@fakemail.com", "123456")
        mockMvc.perform(
            post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.status").value(422))
            .andExpect(jsonPath("$.message").value("Invalid Request"))
            .andExpect(jsonPath("$.internalCode").value("MB-001"))
    }

    @Test
    fun `when try update customer with invalid information should throw error`() {
        val request = UpdateCustomerRequest("", "${Random.nextInt()}@fakemail.com")
        mockMvc.perform(
            put("/customers/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.status").value(422))
            .andExpect(jsonPath("$.message").value("Invalid Request"))
            .andExpect(jsonPath("$.internalCode").value("MB-001"))
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `when delete customer should override customer with status INACTIVE`() {
        val customer = customerRepository.save(buildCustomer())
        mockMvc.perform(delete("/customers/${customer.id}"))
            .andExpect(status().isNoContent)

        val customerDeleted = customerRepository.findById(customer.id!!).get()

        assertEquals(CustomerStatus.INACTIVE, customerDeleted.status)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `when try delete customer not exists should should throw not found exception`() {

        mockMvc.perform(delete("/customers/1"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value("Customer [1] not exists"))
            .andExpect(jsonPath("$.internalCode").value("MB-201"))
    }
}