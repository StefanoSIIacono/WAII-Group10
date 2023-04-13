package com.lab2.server.profiles

import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ProfileController(private val profileService: ProfileService) {
    @GetMapping("/profiles/")
    fun getAll(): List<ProfileDTO>{
        return profileService.getAll()
    }
    @GetMapping("/profiles/{email}")
    fun getProfileByEmail(@PathVariable email:String): ProfileDTO?{
        return profileService.getProfileByEmail(email)
    }

    @PostMapping("/profiles/")
    fun createProfile(@RequestBody profile: ProfileDTO?){
        if (profile!=null)
            profileService.insertProfile(profile)
    }
    @Transactional
    @PutMapping("/profiles/{email}")
    fun changeProfile(@PathVariable email:String, @RequestBody profile: ProfileDTO?){
        if (profile!=null)
            profileService.editProfile(email,profile)
    }
}