package com.lab2.server.serviceImpl

import com.lab2.server.data.Roles
import com.lab2.server.dto.*
import com.lab2.server.exceptionsHandler.exceptions.CannotCreateUserException
import com.lab2.server.exceptionsHandler.exceptions.DuplicateExpertException
import com.lab2.server.exceptionsHandler.exceptions.DuplicateProfileException
import com.lab2.server.exceptionsHandler.exceptions.WrongCredentialsException
import com.lab2.server.security.JwtAuthConverterProperties
import com.lab2.server.services.ExpertService
import com.lab2.server.services.ManagerService
import com.lab2.server.services.ProfileService
import com.lab2.server.services.SecurityService
import org.hibernate.query.sqm.tree.SqmNode
import org.keycloak.admin.client.CreatedResponseUtil
import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.ClientRepresentation
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.core.env.Environment
import org.springframework.http.MediaType
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import javax.ws.rs.NotAuthorizedException

@Service
class SecurityServiceImpl(
    private val profileService: ProfileService,
    private val expertService: ExpertService,
    private val managerService: ManagerService,
    private val keycloak: Keycloak,
    private val env: Environment,
    private val properties: JwtAuthConverterProperties
) : SecurityService {

    override fun signup(profile: CreateProfileDTO) {
        SqmNode.log.info("Creating profile user linked to ${profile.email}")

        val user = UserRepresentation()
        user.isEnabled = true
        user.username = profile.email

        val realmResource = keycloak.realm(env.getProperty("spring.security.oauth2.resourceserver.jwt.realm")!!)
        val usersResource = realmResource.users()

        val response = usersResource.create(user)

        if (response.status != 201) {
            if (response.status == 409) {
                throw DuplicateProfileException("Profile already exist")
            }
            throw CannotCreateUserException("generic error")
        }
        val userid = CreatedResponseUtil.getCreatedId(response)
        val credentials = CredentialRepresentation()
        credentials.isTemporary = false
        credentials.type = CredentialRepresentation.PASSWORD
        credentials.value = profile.password

        val userResource = usersResource.get(userid)

        userResource.resetPassword(credentials)

        val client: ClientRepresentation = realmResource.clients()
            .findByClientId(properties.resourceId)[0]

        val userClientRole = realmResource.clients()[client.id]
            .roles()["PROFILE"].toRepresentation()

        userResource.roles()
            .clientLevel(client.id).add(listOf(userClientRole))

        profileService.insertProfile(ProfileDTO(profile.email, profile.name, profile.surname, profile.address))
        SqmNode.log.info("${user.username} signed in")
    }

    override fun createExpert(expert: CreateExpertDTO) {
        SqmNode.log.info("Creating expert user ${expert.email}")
        val user = UserRepresentation()
        user.isEnabled = true
        user.username = expert.email

        val realmResource = keycloak.realm(env.getProperty("spring.security.oauth2.resourceserver.jwt.realm")!!)
        val usersResource = realmResource.users()

        val response = usersResource.create(user)

        if (response.status != 201) {
            if (response.status == 409) {
                throw DuplicateExpertException("Expert already exist")
            }
            throw CannotCreateUserException("generic error")
        }
        val userid = CreatedResponseUtil.getCreatedId(response)
        val credentials = CredentialRepresentation()
        credentials.isTemporary = false
        credentials.type = CredentialRepresentation.PASSWORD
        credentials.value = expert.password

        val userResource = usersResource.get(userid)

        userResource.resetPassword(credentials)

        val client: ClientRepresentation = realmResource.clients()
            .findByClientId(properties.resourceId)[0]

        val userClientRole = realmResource.clients()[client.id]
            .roles()["EXPERT"].toRepresentation()

        userResource.roles()
            .clientLevel(client.id).add(listOf(userClientRole))

        expertService.insertExpert(
            ExpertDTO(
                expert.email,
                expert.name,
                expert.surname,
                expert.expertises.map { ExpertiseDTO(it) }.toMutableSet()
            )
        )
    }

    override fun login(login: LoginDTO): TokenDTO {
        try {
            SqmNode.log.info("Logging in ${login.username}")
            val client: WebClient = WebClient
                .create(env.getProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri")!!)

            val response = client.post().uri("/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(
                    BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", properties.resourceId!!)
                        .with("username", login.username)
                        .with("password", login.password)
                )
                .retrieve()
                .bodyToMono(TokenDTO::class.java)
                .block()
            return response ?: throw WrongCredentialsException("Wrong username or password")
        } catch (e: Exception) {
            throw WrongCredentialsException("Wrong username or password")
        }
    }

    override fun getLoggedUserInfo(principal: JwtAuthenticationToken): MeDTO {
        val userRole = Roles.values()
            .firstOrNull { sc -> principal.authorities.map { it.authority }.contains(sc.name) }
        SqmNode.log.info("PRINCIPAL: ${principal.name} (ROLE: $userRole)")
        if (userRole === Roles.MANAGER) {
            val manager = managerService.unsafeGetManager(principal.name)!!
            return MeDTO(principal.name, userRole, manager.name, manager.surname)
        } else if (userRole === Roles.EXPERT) {
            val expert = expertService.unsafeGetExpertByEmail(principal.name)!!
            return MeDTO(
                principal.name,
                userRole,
                expert.name,
                expert.surname,
                expert.expertises.map { it.toDTO() }.toMutableSet()
            )
        } else if (userRole === Roles.PROFILE) {
            val profile = profileService.unsafeProfileByEmail(principal.name)!!
            return MeDTO(principal.name, userRole, profile.name, profile.surname, address = profile.address.toDTO())
        }


        throw NotAuthorizedException("Not authorized")
    }
}