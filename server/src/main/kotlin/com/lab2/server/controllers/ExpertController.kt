package com.lab2.server.controllers

import com.lab2.server.data.toExpert
import com.lab2.server.data.toExpertise
import com.lab2.server.dto.ExpertDTO
import com.lab2.server.dto.ExpertiseDTO
import com.lab2.server.exceptionsHandler.exceptions.ExpertNotFoundException
import com.lab2.server.exceptionsHandler.exceptions.ExpertiseNotFoundException
import com.lab2.server.exceptionsHandler.exceptions.NoBodyProvidedException
import com.lab2.server.services.ExpertService
import com.lab2.server.services.ExpertiseService
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class ExpertController(private val expertService: ExpertService, private val expertiseService: ExpertiseService) {

    @GetMapping("/experts/")
    @ResponseStatus(HttpStatus.OK)
    fun getAll(): MutableSet<ExpertDTO> {
        return expertService.getAll()
    }

    @GetMapping("/experts/{expertId}/")
    @ResponseStatus(HttpStatus.OK)
    fun getById(@PathVariable expertId: Long): ExpertDTO? {
        return expertService.getExpertById(expertId) ?: throw ExpertNotFoundException("Expert not found")
    }

    @PostMapping("/experts/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createExpert(@RequestBody expert: ExpertDTO?){
        if (expert == null) {
            throw NoBodyProvidedException("You have to add a body")
        }
        expertService.insertExpert(expert.name, expert.surname)
    }

    @PutMapping("/{expertId}/addExpertise")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    fun addExpertise(@PathVariable expertId: Long, @RequestBody field: String) {
        val expertise = expertiseService.getExpertise(field)
                ?: throw ExpertiseNotFoundException("Expertise not found")
        val expert = expertService.getExpertById(expertId)
                ?: throw ExpertNotFoundException("Expert not found")
        //val expertiseId = expertise.
        expertService.addExpertiseToExpert(expert, expertise)
    }
}