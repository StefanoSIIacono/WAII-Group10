package com.lab2.server.profiles

import com.lab2.server.exceptionsHandler.exceptions.ProfileNotFoundException
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
    fun createProfile(@RequestBody profile: ProfileDTO?){
        if (profile!=null)
            profileService.insertProfile(profile)

    }
    @Transactional
    @PutMapping("/profiles/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changeProfile(@PathVariable email:String, @RequestBody profile: ProfileDTO?){
        if (profile!=null)
            profileService.editProfile(email,profile)
    }
}