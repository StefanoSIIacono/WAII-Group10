package com.lab2.server.controllers

import com.lab2.server.dto.ExpertDTO
import com.lab2.server.dto.ExpertiseDTO
import com.lab2.server.exceptionsHandler.exceptions.ExpertiseNotFoundException
import com.lab2.server.services.ExpertiseService
import org.springframework.http.HttpStatus
import com.lab2.server.exceptionsHandler.exceptions.noBodyProvidedException
import org.springframework.web.bind.annotation.*

@RestController
class ExpertiseController(private val expertiseService: ExpertiseService) {

    @GetMapping("/expertise/")
    @ResponseStatus(HttpStatus.OK)
    fun getAll(): List<ExpertiseDTO>{
        return expertiseService.getAll()
    }

    @GetMapping("/expertise/{expertise}")
    @ResponseStatus(HttpStatus.OK)
    fun getExpertise(@PathVariable expertise: String): ExpertiseDTO{
        return expertiseService.getExpertise(expertise)
            ?: throw ExpertiseNotFoundException("Expertise not found")
    }

    @GetMapping("/expertise/{expertise}/experts/")
    @ResponseStatus(HttpStatus.OK)
    fun getExpertsByExpertise(@PathVariable expertise: String): List<ExpertDTO> {
        return expertiseService.getExpertsByExpertise(expertise)
    }

    @PostMapping("/expertise/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createExpertise(@RequestBody expertise: ExpertiseDTO?){
        if (expertise === null) {
            throw noBodyProvidedException("You have to add a body")
        }
        expertiseService.createExpertise(expertise.field)

    }

    @DeleteMapping("/expertise/{expertise}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteExpertise(@PathVariable expertise: ExpertiseDTO){
        expertiseService.deleteExpertise(expertise.field)
    }
}