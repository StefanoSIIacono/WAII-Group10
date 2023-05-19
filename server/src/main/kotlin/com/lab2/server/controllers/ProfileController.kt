package com.lab2.server.controllers

import com.lab2.server.dto.*
import com.lab2.server.exceptionsHandler.exceptions.NoBodyProvidedException
import com.lab2.server.exceptionsHandler.exceptions.ProfileNotFoundException
import com.lab2.server.services.ProfileService
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
class ProfileController(private val profileService: ProfileService) {
    @GetMapping("/profiles/")
    @ResponseStatus(HttpStatus.OK)
    fun getAll(): List<ProfileDTO>{
        return profileService.getAll()
    }
    @GetMapping("/profiles/{email}")
    @ResponseStatus(HttpStatus.OK)
    fun getProfileByEmail(@PathVariable email:String): ProfileDTO {
        return profileService.getProfileByEmail(email)
            ?: throw ProfileNotFoundException("Profile not found")
    }

    @PostMapping("/profiles/")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    fun createProfile(@RequestBody profile: ProfileDTO?){
        // FIX EXCPETION HANDLING -> AN EMPTY ADDRESS IS NOT RECOGNIZED
        if (profile === null || profile.address === null) {
            throw NoBodyProvidedException("You have to add a body")
        }
        profileService.insertProfile(profile)
    }

    @Transactional
    @PutMapping("/profiles/{email}/newEmail")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changeProfileInfo(@PathVariable email:String, @RequestBody newProfile: ChangeProfileInfoDTO?){

        if(newProfile === null) throw NoBodyProvidedException("No body provided")

        profileService.editProfileInfo(email, newProfile)
    }
    // New implementation for the changeProfileAddress
    @Transactional
    @PutMapping("/profiles/{email}/newAddress")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changeProfileAddress(@PathVariable email: String,
                             @RequestBody newAddress: GetAddressDTO?){
        if (newAddress === null) throw NoBodyProvidedException("No body provided")

        profileService.editAddressByProfile(email , newAddress)
    }

    @GetMapping("/profiles/{email}/tickets/")
    @ResponseStatus(HttpStatus.OK)
    fun getTicketsByEmail(@PathVariable email:String): MutableList<TicketDTO> {
        return profileService.getTicketsByEmail(email)
    }

}