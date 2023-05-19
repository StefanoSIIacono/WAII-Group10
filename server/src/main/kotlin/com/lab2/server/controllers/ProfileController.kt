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

    /*@PostMapping("/profiles/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createProfile(@RequestBody profile: ProfileDTO?){
        if (profile === null) {
            throw NoBodyProvidedException("You have to add a body")
        }
        profileService.insertProfile(profile)

    }*/
    @PostMapping("/profiles/")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    fun createProfile(@RequestBody profile: ProfileDTO?){//, @RequestBody address: CreateOrChangeProfileAddressDTO?){
        if (profile === null || profile.address === null) {
            throw NoBodyProvidedException("You have to add a body")
        }
        profileService.insertProfile(profile)
    }
    //********************************************************************************************************
    // FIX: before that, there was a function that, provided a DTO, had to change the profile information
    // (name, surname). Since now we have also the address, I've rewritten this one to be compliant to the
    // new DTO for the profile (also the insertProfile is now changed) and also a new function to modify
    // specifically the address of the profile, since the address is another entity and must be managed as well
    @Transactional
    @PutMapping("/profiles/{email}/newEmail")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changeProfileInfo(@PathVariable email:String, @RequestBody newProfile: ProfileDTO?){

        if(newProfile === null) throw NoBodyProvidedException("No body provided")

        val profile = profileService.getProfileByEmail(email) ?: throw ProfileNotFoundException("Profile doesn't exist")
        profileService.editProfileInfo(profile, newProfile)
    }
    // New implementation for the changeProfileAddress
    @Transactional
    @PutMapping("/profiles/{email}/newAddress")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changeProfileAddress(@PathVariable email: String,
                             @RequestBody newAddress: AddressDTO?){
        if (newAddress === null) throw NoBodyProvidedException("No body provided")
        println("prima della get")
        val profile = profileService.getProfileByEmail(email) ?: throw NoBodyProvidedException("Email doesn't exist")

        profileService.editAddressByProfile(profile , newAddress)
    }


    @GetMapping("/profiles/{email}/tickets/")
    @ResponseStatus(HttpStatus.OK)
    fun getTicketsByEmail(@PathVariable email:String): MutableList<TicketDTO> {
        return profileService.getTicketsByEmail(email)
    }

}