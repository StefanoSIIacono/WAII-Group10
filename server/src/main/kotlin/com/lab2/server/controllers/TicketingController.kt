package com.lab2.server.controllers

import com.lab2.server.data.Priority
import com.lab2.server.data.Status
import com.lab2.server.data.StatusChanger
import com.lab2.server.dto.TicketCreateBodyDTO
import com.lab2.server.dto.TicketDTO
import com.lab2.server.dto.TicketInProgressBodyDTO
import com.lab2.server.exceptionsHandler.exceptions.IllegalStatusChangeException
import com.lab2.server.exceptionsHandler.exceptions.NoBodyProvidedException
import com.lab2.server.exceptionsHandler.exceptions.TicketNotFoundException
import com.lab2.server.services.TicketService
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
class TicketingController(private val ticketService: TicketService) {
    @GetMapping("/tickets/")
    @ResponseStatus(HttpStatus.OK)
    @Secured("MANAGER", "EXPERT", "PROFILE")
    fun getAll(): List<TicketDTO>{
        return ticketService.getAll()
    }

    @GetMapping("/tickets/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("MANAGER", "EXPERT", "PROFILE")
    fun getTicketByID(@PathVariable ticketId:Long) : TicketDTO {
        return ticketService.getTicketByID(ticketId)
            ?: throw TicketNotFoundException("Ticket not found")
    }

    // MADE BY THE PROFILE
    @PostMapping("/tickets/")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    @Secured("PROFILE")
    fun createTicket(@RequestBody ticket: TicketCreateBodyDTO?){
        if (ticket === null) {
            throw NoBodyProvidedException("You have to add a body")
        }
        ticketService.insertTicket(ticket)
    }

    @PutMapping("/tickets/{ticketId}/open")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("PROFILE")
    fun openTicket(@PathVariable ticketId:Long){
        ticketService.setTicketStatus(ticketId, Status.OPEN, StatusChanger.PROFILE, null, null)
    }

    // MADE BY THE  MANAGER OR THE EXPERT
    @PutMapping("/tickets/{ticketId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("MANAGER", "EXPERT", "PROFILE")
    @Transactional
    fun closeTicket(principal: Principal, @PathVariable ticketId:Long){
        val token = principal as JwtAuthenticationToken

        val statusChanger = StatusChanger.values().firstOrNull { sc -> token.authorities.map { it.authority }.contains(sc.name) }
        if (statusChanger === null) {
            throw IllegalStatusChangeException("")
        }
        ticketService.setTicketStatus(ticketId, Status.CLOSED, statusChanger, null, null)
    }

    // MADE BY THE PROFILE
    @PutMapping("/tickets/{ticketId}/reopen")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("PROFILE")
    @Transactional
    fun reopenTicket(@PathVariable ticketId:Long){
        ticketService.setTicketStatus(ticketId, Status.REOPENED, StatusChanger.PROFILE, null, null)
    }

    // MADE BY THE PROFILE
    @PutMapping("/tickets/{ticketId}/resolved")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("PROFILE")
    @Transactional
    fun resolveTicket(@PathVariable ticketId:Long){
        ticketService.setTicketStatus(ticketId, Status.RESOLVED, StatusChanger.PROFILE, null, null)
    }

    // MADE BY THE MANAGER
    @PutMapping("/tickets/{ticketId}/inprogress")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("MANAGER")
    @Transactional
    fun progressTicket(@PathVariable ticketId:Long, @RequestBody body: TicketInProgressBodyDTO?){
        if (body === null) {
            throw NoBodyProvidedException("Wrong body")
        }
        ticketService.setTicketStatus(ticketId, Status.IN_PROGRESS, StatusChanger.MANAGER, body.expert, body.priority)
    }

    @PutMapping("/tickets/{ticketId}/set_priority/{priority}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("MANAGER")
    fun setTicketPriority(@PathVariable ticketId:Long, @PathVariable priority: Priority){
        ticketService.setTicketPriority(ticketId, priority)
    }
}