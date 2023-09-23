package com.lab2.server.security

import jakarta.servlet.http.HttpServletRequest
import lombok.RequiredArgsConstructor
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.http.HttpHeaders
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.util.WebUtils


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
class WebSecurityConfig(
    private val jwtAuthConverter: JwtAuthConverter,
    private val env: Environment,
) {

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
            .and().bearerTokenResolver(this::tokenExtractor)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        return http.build()
    }

    fun tokenExtractor(request: HttpServletRequest): String? {
        if (request.requestURI.startsWith("/login")) {
            return null
        }
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (header != null) return header.replace("Bearer ", "")
        return WebUtils.getCookie(request, "token")?.value
    }

    @Bean
    fun keycloak(): Keycloak {
        return KeycloakBuilder.builder()
            .serverUrl(env.getProperty("spring.security.oauth2.resourceserver.jwt.url")!!)
            .realm("master")
            .clientId("admin-cli")
            .username("admin")
            .password("admin")
            .build()
    }
}