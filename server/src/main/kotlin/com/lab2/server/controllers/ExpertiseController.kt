package com.lab2.server.controllers

import com.lab2.server.dto.ExpertDTO
import com.lab2.server.dto.ExpertiseDTO
import com.lab2.server.exceptionsHandler.exceptions.ExpertiseNotFoundException
import com.lab2.server.exceptionsHandler.exceptions.NoBodyProvidedException
import com.lab2.server.services.ExpertiseService
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

@RestController
class ExpertiseController(private val expertiseService: ExpertiseService) {

    @GetMapping("/expertises/")
    @ResponseStatus(HttpStatus.OK)
    @Secured("MANAGER", "EXPERT", "PROFILE")
    fun getAll(): List<ExpertiseDTO>{
        return expertiseService.getAll()
    } // THE PROFILE CAN TAKE A LOOK ON THE EXPERTISES FOR TICKET ARGUMENT

    @GetMapping("/expertises/{field}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("MANAGER", "EXPERT", "PROFILE")
    fun getExpertise(@PathVariable field: String): ExpertiseDTO{
        return expertiseService.getExpertise(field)
            ?: throw ExpertiseNotFoundException("Expertise not found")
    } // USELESS: CAN BE DELETED

    @GetMapping("/expertises/{field}/experts/")
    @ResponseStatus(HttpStatus.OK)
    @Secured("MANAGER")
    fun getExpertsByExpertise(@PathVariable field: String): List<ExpertDTO> {
        return expertiseService.getExpertsByExpertise(field)
    }

    @PostMapping("/expertises/")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured("MANAGER")
    fun createExpertise(@RequestBody expertise: ExpertiseDTO?){
        if (expertise === null) {
            throw NoBodyProvidedException("You have to add a body")
        }
        expertiseService.createExpertise(expertise.field)
    }

    @DeleteMapping("/expertises/{expertise}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("MANAGER")
    fun deleteExpertise(@PathVariable expertise: String){
        expertiseService.deleteExpertise(expertise)
    }
}