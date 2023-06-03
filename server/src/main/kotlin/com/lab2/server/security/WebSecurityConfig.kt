package com.lab2.server.security

import lombok.RequiredArgsConstructor
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled=true, securedEnabled = true)
class WebSecurityConfig (private val properties: JwtAuthConverterProperties, private val jwtAuthConverter: JwtAuthConverter, private val env: Environment,) {

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable()
        http.authorizeHttpRequests()
                .anyRequest()
                .permitAll()
        http.oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(jwtAuthConverter)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        return http.build()
    }

    @Bean
    fun keycloak(): Keycloak {
        return KeycloakBuilder.builder()
            .serverUrl(env.getProperty("spring.security.oauth2.resourceserver.jwt.url")!!)
            .realm(env.getProperty("spring.security.oauth2.resourceserver.jwt.realm")!!)
            .clientId(properties.resourceId)
            .username("profile")
            .password("password")
            .build()
    }

    companion object {
        const val MANAGER = "MANAGER"
        const val PROFILE = "PROFILE"
        const val EXPERT = "EXPERT"
    }
}