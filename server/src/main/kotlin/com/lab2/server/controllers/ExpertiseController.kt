package com.lab2.server.controllers

import com.lab2.server.dto.ExpertDTO
import com.lab2.server.dto.ExpertiseDTO
import com.lab2.server.dto.PagedDTO
import com.lab2.server.services.ExpertiseService
import io.micrometer.observation.annotation.Observed
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
class ExpertiseController(private val expertiseService: ExpertiseService) {

    @GetMapping("/expertises")
    @ResponseStatus(HttpStatus.OK)
    @Secured("MANAGER", "PROFILE")
    fun getAllPaginated(
        @RequestParam(required = false, defaultValue = "1") @Min(1) page: Int,
        @RequestParam(required = false, defaultValue = "100") @Min(1) @Max(100) offset: Int,
    ): PagedDTO<ExpertiseDTO> {
        log.info("Retrieving all expertises")
        return expertiseService.getAllPaginated(page - 1, offset)
    } // THE PROFILE CAN TAKE A LOOK ON THE EXPERTISES FOR TICKET ARGUMENT

    @GetMapping("/expertises/search/{field}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("MANAGER", "PROFILE")
    fun searchByFieldPaginated(
        @PathVariable field: String,
        @RequestParam(required = false, defaultValue = "1") @Min(1) page: Int,
        @RequestParam(required = false, defaultValue = "100") @Min(1) @Max(100) offset: Int,
    ): PagedDTO<ExpertiseDTO> {
        log.info("Retrieving all expertises")
        return expertiseService.searchByFieldPaginated(field, page - 1, offset)
    }

    @GetMapping("/expertises/{field}/experts")
    @ResponseStatus(HttpStatus.OK)
    @Secured("MANAGER")
    fun getExpertsByExpertise(
        @PathVariable field: String,
        @RequestParam(required = false, defaultValue = "1") @Min(1) page: Int,
        @RequestParam(required = false, defaultValue = "100") @Min(1) @Max(100) offset: Int,
    ): PagedDTO<ExpertDTO> {
        log.info("Retrieving all experts with expertise $field")
        return expertiseService.getExpertsByExpertisePaginated(field, page - 1, offset)
    }

    @PostMapping("/expertises")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured("MANAGER")
    fun createExpertise(@RequestBody(required = true) expertise: ExpertiseDTO) {
        log.info("Adding new expertise")
        expertiseService.createExpertise(expertise.field)
    }

    @DeleteMapping("/expertises/{field}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("MANAGER")
    fun deleteExpertise(@PathVariable field: String) {
        log.info("Deleting expertise $field")
        expertiseService.deleteExpertise(field)
    }
}