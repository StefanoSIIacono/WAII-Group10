package com.lab2.server.profiles

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ProfileController(private val productService: ProfileService) {
    @GetMapping("/profiles/")
    fun getAll(): List<ProfileDTO>{
        return productService.getAll()
    }
    @GetMapping("/profiles/{prodileId}")
    fun getProduct(@PathVariable profileId:String): ProfileDTO?{
        return productService.getProduct(profileId)
    }
}