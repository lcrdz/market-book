package com.marketbook.config

import com.marketbook.enums.Role
import com.marketbook.repository.CustomerRepository
import com.marketbook.security.AuthenticationFilter
import com.marketbook.security.AuthorizationFilter
import com.marketbook.security.CustomAuthenticationEntryPoint
import com.marketbook.security.JwtUtil
import com.marketbook.service.UserDetailsCustomService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

private val PUBLIC_POST_MATCHERS = arrayOf(
    "/customers"
)

private val ADMIN_MATCHERS = arrayOf(
    "/admin/**"
)

private val PUBLIC_GET_MATCHERS = arrayOf(
    "/books"
)

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val customerRepository: CustomerRepository,
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val userDetails: UserDetailsCustomService,
    private val jwtUtil: JwtUtil,
    private val customAuthenticationEntryPoint: CustomAuthenticationEntryPoint
) : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetails).passwordEncoder(bCryptPasswordEncoder())
    }

    override fun configure(http: HttpSecurity) {
        http.run {
            cors().and().csrf().disable()
            authorizeRequests()
                .antMatchers(HttpMethod.POST, *PUBLIC_POST_MATCHERS).permitAll()
                .antMatchers(HttpMethod.GET, *PUBLIC_GET_MATCHERS).permitAll()
                .antMatchers(*ADMIN_MATCHERS).hasAuthority(Role.ADMIN.description).anyRequest().authenticated()
            addFilter(AuthenticationFilter(authenticationManager(), customerRepository, jwtUtil))
            addFilter(AuthorizationFilter(authenticationManager(), userDetails, jwtUtil))
            sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint)
        }
    }

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers(
            "/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/**",
            "/swagger-ui.html",
            "/webjars/**"
        )
    }

    @Bean
    fun cors(): CorsFilter {
        val config = CorsConfiguration().apply {
            allowCredentials = true
            addAllowedOriginPattern("*")
            addAllowedHeader("*")
            addAllowedMethod("*")
        }
        val source = UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", config)
        }
        return CorsFilter(source)
    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    override fun authenticationManager(): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

}