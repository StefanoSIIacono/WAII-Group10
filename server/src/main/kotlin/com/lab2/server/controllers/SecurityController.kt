package com.lab2.server.controllers

import com.lab2.server.dto.CreateProfileDTO
import com.lab2.server.dto.LoginDTO
import com.lab2.server.dto.ProfileDTO
import com.lab2.server.dto.TokenDTO
import com.lab2.server.exceptionsHandler.exceptions.CannotCreateUserException
import com.lab2.server.exceptionsHandler.exceptions.NoBodyProvidedException
import com.lab2.server.exceptionsHandler.exceptions.WrongCredentialsException
import com.lab2.server.security.JwtAuthConverterProperties
import com.lab2.server.services.ProfileService
import org.keycloak.admin.client.CreatedResponseUtil
import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.ClientRepresentation
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import java.util.*


@RestController
class SecurityController(private val profileService: ProfileService, private val keycloak: Keycloak, private val env: Environment, private val properties: JwtAuthConverterProperties) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    fun signup(@RequestBody body: CreateProfileDTO?) {
        if (body === null) {
            throw NoBodyProvidedException("You have to add a body")
        }
        val user = UserRepresentation()
        user.isEnabled = true
        user.username = body.email

        val realmResource = keycloak.realm(env.getProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri")!!)
        val usersResource = realmResource.users()

        val response = usersResource.create(user)

        if (response.status != 200) {
            throw CannotCreateUserException("generic error")
        }
        val userid = CreatedResponseUtil.getCreatedId(response)
        val credentials = CredentialRepresentation()
        credentials.isTemporary = false
        credentials.type = CredentialRepresentation.PASSWORD
        credentials.value = body.password

        val userResource = usersResource.get(userid)

        userResource.resetPassword(credentials)

        val client: ClientRepresentation = realmResource.clients()
            .findByClientId(properties.resourceId)[0]

        val userClientRole = realmResource.clients()[client.id]
            .roles()["PROFILE"].toRepresentation()

        userResource.roles()
            .clientLevel(client.id).add(listOf(userClientRole))

        profileService.insertProfile(ProfileDTO(body.email, body.name, body.surname, body.address))
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/createExpert")
    fun createExpert(@RequestBody body: LoginDTO?) {
        if (body === null) {
            throw NoBodyProvidedException("You have to add a body")
        }
        val user = UserRepresentation()
        user.isEnabled = true
        user.username = body.username

        val realmResource = keycloak.realm(env.getProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri")!!)
        val usersResource = realmResource.users()

        val response = usersResource.create(user)

        if (response.status != 200) {
            throw CannotCreateUserException("generic error")
        }
        val userid = CreatedResponseUtil.getCreatedId(response)
        val credentials = CredentialRepresentation()
        credentials.isTemporary = false
        credentials.type = CredentialRepresentation.PASSWORD
        credentials.value = body.password

        val userResource = usersResource.get(userid)

        userResource.resetPassword(credentials)

        val client: ClientRepresentation = realmResource.clients()
            .findByClientId(properties.resourceId)[0]

        val userClientRole = realmResource.clients()[client.id]
            .roles()["PROFILE"].toRepresentation()

        userResource.roles()
            .clientLevel(client.id).add(listOf(userClientRole))

        // TODO: add to db
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login/")
    fun login(@RequestBody body: LoginDTO?): TokenDTO {
        if (body === null) {
            throw NoBodyProvidedException("You have to add a body")
        }
        try {
            val client: WebClient = WebClient
                    .create(env.getProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri")!!)

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

    @GetMapping("/prova")
    @ResponseStatus(HttpStatus.OK)
    fun getMe(principal: JwtAuthenticationToken): String {

        return principal.tokenAttributes["preferred_username"].toString()
    }
}