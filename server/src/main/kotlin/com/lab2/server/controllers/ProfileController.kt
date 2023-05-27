package com.lab2.server.controllers

import com.lab2.server.dto.*
import com.lab2.server.exceptionsHandler.exceptions.NoBodyProvidedException
import com.lab2.server.exceptionsHandler.exceptions.ProfileNotFoundException
import com.lab2.server.services.ProfileService
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
class ProfileController(private val profileService: ProfileService) {
    @GetMapping("/profiles/")
    @ResponseStatus(HttpStatus.OK)
    @Secured("MANAGER", "EXPERT", "PROFILE")
    fun getAll(): List<ProfileDTO>{
        return profileService.getAll()
    } // MANAGER ONLY

    @GetMapping("/profiles/{email}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("MANAGER", "EXPERT", "PROFILE")
    fun getProfileByEmail(@PathVariable email:String): ProfileDTO {
        return profileService.getProfileByEmail(email)
            ?: throw ProfileNotFoundException("Profile not found")
    } // ADD A CHECK FOR PROFILE -> COMPARE JWT EMAIL WITH SEARCHED ONE
        // EXCLUDE EXPERT

    @PostMapping("/profiles/")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    fun createProfile(@RequestBody profile: ProfileDTO?){
        // FIX EXCEPTION HANDLING -> AN EMPTY ADDRESS IS NOT RECOGNIZED (SERVICE SIDE)
        if (profile === null || profile.address === null) {
            throw NoBodyProvidedException("You have to add a body")
        }
        profileService.insertProfile(profile)
    } // IMPROVE SIGN IN WITH KEYCLOAK AND SO ON

    @Transactional
    @PutMapping("/profiles/{email}/newInfo")
    @Secured("PROFILE")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changeProfileInfo(@PathVariable email:String, @RequestBody newProfile: ChangeProfileInfoDTO?){

        if(newProfile === null) throw NoBodyProvidedException("No body provided")

        profileService.editProfileInfo(email, newProfile)
    }

    @Transactional
    @PutMapping("/profiles/{email}/newAddress")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("PROFILE")
    fun changeProfileAddress(@PathVariable email: String,
                             @RequestBody newAddress: GetAddressDTO?){
        if (newAddress === null) throw NoBodyProvidedException("No body provided")

        profileService.editAddressByProfile(email , newAddress)
    }

    @GetMapping("/profiles/{email}/tickets/")
    @ResponseStatus(HttpStatus.OK)
    @Secured("PROFILE")
    fun getTicketsByEmail(@PathVariable email:String): MutableList<TicketDTO> {
        profileService.getProfileByEmail(email) ?: throw ProfileNotFoundException("Profile not found")
        return profileService.getTicketsByEmail(email)
    } // PROFILE AND MANAGER ONLY

}