package com.lab2.server.security

import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
class WebSecurityConfig (private val jwtAuthConverter: JwtAuthConverter) {

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests()
            .requestMatchers(HttpMethod.GET, "/login").permitAll()
            .requestMatchers(HttpMethod.GET, "/test/admin", "/test/admin/**").hasRole(ADMIN)
            .requestMatchers(HttpMethod.GET, "/test/user").hasAnyRole(ADMIN, USER)
            .anyRequest().authenticated()
        http.oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(jwtAuthConverter)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        return http.build()
    }

    companion object {
        const val ADMIN = "admin"
        const val USER = "USER"
    }
}