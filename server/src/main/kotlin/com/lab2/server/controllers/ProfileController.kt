package com.lab2.server.controllers

import com.lab2.server.dto.*
import com.lab2.server.exceptionsHandler.exceptions.NoBodyProvidedException
import com.lab2.server.exceptionsHandler.exceptions.ProfileNotFoundException
import com.lab2.server.services.ProfileService
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
class ProfileController(private val profileService: ProfileService) {
    @GetMapping("/profiles/")
    @ResponseStatus(HttpStatus.OK)
    //@Secured("MANAGER", "EXPERT", "PROFILE")
    fun getAll(): List<ProfileDTO>{
        log.info("Retrieving all profiles")
        return profileService.getAll()
    } // MANAGER ONLY

    @GetMapping("/profiles/{email}")
    @ResponseStatus(HttpStatus.OK)
    //@Secured("MANAGER", "EXPERT", "PROFILE")
    fun getProfileByEmail(@PathVariable email:String): ProfileDTO {
        log.info("Retrieving profile linked to $email")
        return profileService.getProfileByEmail(email)
            ?: throw ProfileNotFoundException("Profile not found")
    } // ADD A CHECK FOR PROFILE -> COMPARE JWT EMAIL WITH SEARCHED ONE
        // EXCLUDE EXPERT

    @PostMapping("/profiles/")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    fun createProfile(@RequestBody profile: ProfileDTO?){
        log.info("Creating profile linked to ${profile?.email}")
        // FIX EXCEPTION HANDLING -> AN EMPTY ADDRESS IS NOT RECOGNIZED (SERVICE SIDE)
        if (profile === null || profile.address === null) {
            log.error("Provided invalid body for profile creation")
            throw NoBodyProvidedException("You have to add a body")
        }
        profileService.insertProfile(profile)
    } // IMPROVE SIGN IN WITH KEYCLOAK AND SO ON

    @Transactional
    @PutMapping("/profiles/{email}/newInfo")
    //@Secured("PROFILE")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changeProfileInfo(@PathVariable email:String, @RequestBody newProfile: ChangeProfileInfoDTO?){
        log.info("Modifying profile linked to $email")
        if(newProfile === null) {
            log.error("Provided invalid body for profile linked to $email")
            throw NoBodyProvidedException("No body provided")
        }

        profileService.editProfileInfo(email, newProfile)
    }

    @Transactional
    @PutMapping("/profiles/{email}/newAddress")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("PROFILE")
    fun changeProfileAddress(@PathVariable email: String,
                             @RequestBody newAddress: GetAddressDTO?){
        log.info("Changing profile address linked to $email")
        if (newAddress === null) {
            log.error("Invalid body provided")
            throw NoBodyProvidedException("No body provided")
        }

        profileService.editAddressByProfile(email , newAddress)
    }

    @GetMapping("/profiles/{email}/tickets/")
    @ResponseStatus(HttpStatus.OK)
    @Secured("PROFILE")
    fun getTicketsByEmail(@PathVariable email:String): MutableList<TicketDTO> {
        log.info("Retrieving all ticket for profile linked to $email")
        profileService.getProfileByEmail(email) ?: throw ProfileNotFoundException("Profile not found")
        return profileService.getTicketsByEmail(email)
    } // PROFILE AND MANAGER ONLY

}