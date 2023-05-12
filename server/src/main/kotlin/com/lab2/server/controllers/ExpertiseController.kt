package com.lab2.server.controllers

import com.lab2.server.dto.ExpertDTO
import com.lab2.server.dto.ExpertiseDTO
import com.lab2.server.exceptionsHandler.exceptions.ExpertiseNotFoundException
import com.lab2.server.services.ExpertiseService
import org.springframework.http.HttpStatus
import com.lab2.server.exceptionsHandler.exceptions.NoBodyProvidedException
import org.springframework.web.bind.annotation.*

@RestController
class ExpertiseController(private val expertiseService: ExpertiseService) {

    @GetMapping("/expertises/")
    @ResponseStatus(HttpStatus.OK)
    fun getAll(): List<ExpertiseDTO>{
        return expertiseService.getAll()
    }

    @GetMapping("/expertises/{field}")
    @ResponseStatus(HttpStatus.OK)
    fun getExpertise(@PathVariable field: String): ExpertiseDTO{
        return expertiseService.getExpertise(field)
            ?: throw ExpertiseNotFoundException("Expertise not found")
    }

    @GetMapping("/expertises/{field}/experts/")
    @ResponseStatus(HttpStatus.OK)
    fun getExpertsByExpertise(@PathVariable field: String): List<ExpertDTO> {
        return expertiseService.getExpertsByExpertise(field)
    }

    @PostMapping("/expertises/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createExpertise(@RequestBody expertise: ExpertiseDTO?){
        if (expertise === null) {
            throw NoBodyProvidedException("You have to add a body")
        }
        expertiseService.createExpertise(expertise.field)

    }

    @DeleteMapping("/expertises/{expertise}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteExpertise(@PathVariable expertise: String){
        expertiseService.deleteExpertise(expertise)
    }
}