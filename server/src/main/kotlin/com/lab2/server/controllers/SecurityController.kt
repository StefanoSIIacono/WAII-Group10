package com.lab2.server.controllers

import com.lab2.server.dto.LoginDTO
import com.lab2.server.dto.TokenDTO
import com.lab2.server.exceptionsHandler.exceptions.NoBodyProvidedException
import com.lab2.server.exceptionsHandler.exceptions.WrongCredentialsException
import com.lab2.server.security.JwtAuthConverterProperties
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient

@ResponseStatus(HttpStatus.CREATED)
@RestController
class SecurityController(private val env: Environment, private val properties: JwtAuthConverterProperties) {
    @PostMapping("/login/")
    fun login(@RequestBody body: LoginDTO?): TokenDTO {
        if (body === null) {
            throw NoBodyProvidedException("You have to add a body")
        }
        try {
            val client: WebClient = WebClient.create(env.getProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri")!!)

            val response = client.post().uri("/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(
                    BodyInserters.fromFormData("grant_type", "password")
                    .with("client_id", properties.resourceId!!)
                    .with("username", body.username)
                    .with("password", body.password)
                )
                .retrieve()
                .bodyToMono(TokenDTO::class.java)
                .block()
            println(response)
            return response ?: throw WrongCredentialsException("Wrong username or password")
        } catch (e: Exception) {
            throw WrongCredentialsException("Wrong username or password")
        }
    }
}