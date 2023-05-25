package com.lab2.server.security

import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
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
        // http.authorizeHttpRequests()
        //    .requestMatchers(HttpMethod.GET, "/login", "/products").permitAll()
        //    .requestMatchers(HttpMethod.GET, "/experts/**", "/expertises/**").hasRole(MANAGER)
        //    .requestMatchers(HttpMethod.GET, "/profiles", "/tickets").hasAnyRole(PROFILE, MANAGER, EXPERT)
        //    .anyRequest().authenticated()

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