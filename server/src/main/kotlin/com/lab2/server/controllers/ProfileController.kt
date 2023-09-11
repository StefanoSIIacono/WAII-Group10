package com.lab2.server.controllers

import com.lab2.server.dto.*
import com.lab2.server.services.ProfileService
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
class ProfileController(private val profileService: ProfileService) {
    @GetMapping("/profiles")
    @ResponseStatus(HttpStatus.OK)
    @Secured("MANAGER")
    fun getAllPaginated(
        @RequestParam(required = false, defaultValue = "1") @Min(1) page: Int,
        @RequestParam(required = false, defaultValue = "100") @Min(1) @Max(100) offset: Int,
    ): PagedDTO<ProfileDTO> {
        log.info("Retrieving all profiles")
        return profileService.getAllPaginated(page - 1, offset)
    }

    @GetMapping("/profiles/{email}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("MANAGER")
    fun getProfileByEmail(@PathVariable email: String): ProfileDTO {
        log.info("Retrieving profile linked to $email")
        return profileService.getProfileByEmail(email)
    }

    @Transactional
    @PutMapping("/profiles/edit")
    @Secured("PROFILE")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun editProfile(@RequestBody(required = true) newProfile: ChangeProfileInfoDTO, principal: JwtAuthenticationToken) {
        log.info("Modifying profile linked to ${principal.name}")
        profileService.editProfileInfo(principal, newProfile)
    }

    @GetMapping("/profiles/{email}/tickets")
    @ResponseStatus(HttpStatus.OK)
    @Secured("PROFILE", "MANAGER")
    fun getTicketsByEmailPaginated(
        @PathVariable email: String,
        @RequestParam(required = false, defaultValue = "1") @Min(1) page: Int,
        @RequestParam(required = false, defaultValue = "100") @Min(1) @Max(100) offset: Int,
        principal: JwtAuthenticationToken
    ): PagedDTO<TicketDTO> {
        log.info("Retrieving all ticket for profile linked to $email")
        return profileService.getTicketsByEmailPaginated(email, page - 1, offset, principal)
    }

}