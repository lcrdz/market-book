package com.marketbook.service

import com.marketbook.exception.AuthenticationException
import com.marketbook.repository.CustomerRepository
import com.marketbook.security.UserCustomDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsCustomService(
    private val customerRepository: CustomerRepository
) : UserDetailsService {

    override fun loadUserByUsername(id: String): UserDetails {
        val customer = customerRepository.findById(id.toInt())
            .orElseThrow { AuthenticationException("User not found", "999") }
        return UserCustomDetails(customer)
    }
}