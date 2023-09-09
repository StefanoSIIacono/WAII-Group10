package com.lab2.server.controllers

import com.lab2.server.data.Priority
import com.lab2.server.data.Status
import com.lab2.server.dto.PagedDTO
import com.lab2.server.dto.TicketCreateBodyDTO
import com.lab2.server.dto.TicketDTO
import com.lab2.server.dto.TicketInProgressBodyDTO
import com.lab2.server.services.TicketService
import io.micrometer.observation.annotation.Observed
import jakarta.transaction.Transactional
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import lombok.extern.slf4j.Slf4j
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@Slf4j
@Observed
class TicketingController(private val ticketService: TicketService) {
    @GetMapping("/tickets")
    @ResponseStatus(HttpStatus.OK)
    @Secured("MANAGER", "EXPERT", "PROFILE")
    fun getAllPaged(
        @RequestParam(required = false, defaultValue = "1") @Min(1) page: Int,
        @RequestParam(required = false, defaultValue = "100") @Min(1) @Max(100) offset: Int,
        principal: JwtAuthenticationToken
    ): PagedDTO<TicketDTO> {
        log.info("Retrieving all tickets")
        return ticketService.getAllPaginated(page, offset, principal)
    }

    @GetMapping("/tickets/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("MANAGER", "EXPERT", "PROFILE")
    fun getTicketByID(@PathVariable ticketId: Long, principal: JwtAuthenticationToken): TicketDTO {
        log.info("Retrieving ticket $ticketId")
        return ticketService.getTicketByID(ticketId, principal)
    }

    @PostMapping("/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    @Secured("PROFILE")
    fun createTicket(@RequestBody(required = true) ticket: TicketCreateBodyDTO, principal: JwtAuthenticationToken) {
        log.info("Creating new ticket")
        ticketService.insertTicket(ticket, principal)
    }

    @PutMapping("/tickets/{ticketId}/open")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("PROFILE")
    @Transactional
    fun reOpenTicket(@PathVariable ticketId: Long, principal: JwtAuthenticationToken) {
        log.info("Reopening ticket  $ticketId")
        ticketService.setTicketStatus(
            principal,
            ticketId,
            Status.REOPENED,
        )
    }

    @PutMapping("/tickets/{ticketId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("MANAGER", "EXPERT", "PROFILE")
    @Transactional
    fun closeTicket(@PathVariable ticketId: Long, principal: JwtAuthenticationToken) {
        log.info("Changing status for ticket $ticketId")
        ticketService.setTicketStatus(
            principal,
            ticketId,
            Status.CLOSED
        )
    }

    @PutMapping("/tickets/{ticketId}/resolved")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("PROFILE", "EXPERT")
    @Transactional
    fun resolveTicket(@PathVariable ticketId: Long, principal: JwtAuthenticationToken) {
        log.info("Resolving ticket  $ticketId")
        ticketService.setTicketStatus(
            principal,
            ticketId,
            Status.RESOLVED,
        )
    }

    @PutMapping("/tickets/{ticketId}/inprogress")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("MANAGER")
    @Transactional
    fun inProgressTicket(
        @PathVariable ticketId: Long,
        @RequestBody(required = true) body: TicketInProgressBodyDTO,
        principal: JwtAuthenticationToken
    ) {
        log.info("Setting ticket  $ticketId in progress")
        ticketService.setTicketStatus(
            principal,
            ticketId,
            Status.IN_PROGRESS,
            body.expert,
            body.priority
        )
    }

    @PutMapping("/tickets/{ticketId}/priority/{priority}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("MANAGER")
    fun setTicketPriority(@PathVariable ticketId: Long, @PathVariable priority: Priority) {
        log.info("Setting ticket  $ticketId priority to $priority")
        ticketService.setTicketPriority(ticketId, priority)
    }
}