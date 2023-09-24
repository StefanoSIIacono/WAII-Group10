package com.lab2.server.controllers

import com.lab2.server.dto.ExpertDTO
import com.lab2.server.dto.ExpertiseDTO
import com.lab2.server.dto.PagedDTO
import com.lab2.server.services.ExpertService
import io.micrometer.observation.annotation.Observed
import jakarta.transaction.Transactional
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import lombok.extern.slf4j.Slf4j
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

@RestController
@Slf4j
@Observed
class ExpertController(private val expertService: ExpertService) {

    @GetMapping("/experts")
    @ResponseStatus(HttpStatus.OK)
    @Secured("MANAGER")
    fun getAllPaginated(
        @RequestParam(required = false, defaultValue = "1") @Min(1) page: Int,
        @RequestParam(required = false, defaultValue = "100") @Min(1) @Max(100) offset: Int,
    ): PagedDTO<ExpertDTO> {
        log.info("Retrieving all experts")
        return expertService.getAllPaginated(page - 1, offset)
    }

    @GetMapping("/experts/search/{email}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("MANAGER")
    fun searchByEmailPaginated(
        @PathVariable email: String,
        @RequestParam(required = false) expertise: String?,
        @RequestParam(required = false, defaultValue = "1") @Min(1) page: Int,
        @RequestParam(required = false, defaultValue = "100") @Min(1) @Max(100) offset: Int,
    ): PagedDTO<ExpertDTO> {
        log.info("Retrieving all experts")
        return expertService.searchByEmailAndExpertisePaginated(email, expertise, page - 1, offset)
    }

    @GetMapping("/experts/{email}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("MANAGER")
    fun getById(@PathVariable email: String): ExpertDTO {
        log.info("Retrieving expert linked to $email")
        return expertService.getExpertByEmail(email)
    }

    @PutMapping("/experts/{email}/expertise")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("MANAGER")
    @Transactional
    fun addExpertise(@PathVariable email: String, @RequestBody(required = true) expertise: ExpertiseDTO) {
        expertService.addExpertiseToExpert(email, expertise.field)
    }

    @DeleteMapping("/experts/{email}/expertise")
    @ResponseStatus(HttpStatus.OK)
    @Secured("MANAGER")
    @Transactional
    fun deleteExpertise(@PathVariable email: String, @RequestBody(required = true) expertise: ExpertiseDTO) {
        expertService.removeExpertiseFromExpert(email, expertise.field)
    }
}