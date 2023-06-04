package com.lab2.server.controllers

import com.lab2.server.dto.ExpertDTO
import com.lab2.server.dto.ExpertiseDTO
import com.lab2.server.exceptionsHandler.exceptions.ExpertNotFoundException
import com.lab2.server.exceptionsHandler.exceptions.ExpertiseNotFoundException
import com.lab2.server.exceptionsHandler.exceptions.NoBodyProvidedException
import com.lab2.server.services.ExpertService
import com.lab2.server.services.ExpertiseService
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

@RestController
class ExpertController(private val expertService: ExpertService, private val expertiseService: ExpertiseService) {

    @GetMapping("/experts/")
    @ResponseStatus(HttpStatus.OK)
    @Secured("MANAGER")
    fun getAll(): MutableSet<ExpertDTO> {
        return expertService.getAll()
    }

    @GetMapping("/experts/{expertEmail}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("MANAGER")
    fun getById(@PathVariable expertEmail: String): ExpertDTO? {
        return expertService.getExpertByEmail(expertEmail) ?: throw ExpertNotFoundException("Expert not found")
    }

    @PutMapping("/experts/{expertEmail}/addExpertise")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("MANAGER")
    @Transactional
    fun addExpertise(@PathVariable expertEmail: String, @RequestBody expertise: String?) {
        if (expertise === null) {
            throw NoBodyProvidedException("No body")
        }

        expertService.addExpertiseToExpert(expertEmail, expertise)
    }
}