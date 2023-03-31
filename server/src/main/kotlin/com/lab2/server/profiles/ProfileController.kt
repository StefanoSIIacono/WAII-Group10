package com.lab2.server.profiles

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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
}