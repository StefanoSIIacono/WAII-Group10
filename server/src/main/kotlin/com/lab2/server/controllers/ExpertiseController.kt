package com.lab2.server.controllers

import com.lab2.server.dto.ExpertDTO
import com.lab2.server.dto.ExpertiseDTO
import com.lab2.server.exceptionsHandler.exceptions.ExpertiseNotFoundException
import com.lab2.server.services.ExpertiseService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class ExpertiseController(private val expertiseService: ExpertiseService) {

    @GetMapping("/expertises/")
    @ResponseStatus(HttpStatus.OK)
    fun getAll(): List<ExpertiseDTO>{
        return expertiseService.getAll()
    }

    @GetMapping("/expertise/")
    @ResponseStatus(HttpStatus.OK)
    fun getExpertise(field: String): ExpertiseDTO{
        return expertiseService.getExpertise(field)
            ?: throw ExpertiseNotFoundException("Expertise not found")
    }

    @GetMapping("/{expertise}/experts/")
    @ResponseStatus(HttpStatus.OK)
    fun getExpertsByExpertise(@PathVariable expertise: String): List<ExpertDTO>{
        return expertiseService.getExpertsByExpertise(expertise)
    }
}