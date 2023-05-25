package com.lab2.server.security

import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled=true, securedEnabled = true)
class WebSecurityConfig (private val jwtAuthConverter: JwtAuthConverter) {

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable()
        http.authorizeHttpRequests()
            .requestMatchers(HttpMethod.POST, "/login/").permitAll()
            .requestMatchers(HttpMethod.GET,
                "/",
                "/login",
                "/products",
                "/index.html",
                "/static/**",
                "/background.jpg",
                "/products/**",
                "/login",
                "/notFound").permitAll()
            .requestMatchers(HttpMethod.GET,
                "/experts/**",
                "/expertises/**").hasRole(MANAGER)
            .requestMatchers(HttpMethod.GET,
                "/profiles",
                "/profiles/**",
                "/tickets",
                "/tickets/**").hasAnyRole(PROFILE, MANAGER, EXPERT)
            .anyRequest().authenticated()
        http.oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(jwtAuthConverter)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        return http.build()
    }

    companion object {
        const val MANAGER = "MANAGER"
        const val PROFILE = "PROFILE"
        const val EXPERT = "EXPERT"
    }
}