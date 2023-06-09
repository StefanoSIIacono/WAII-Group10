package com.lab2.server.controllers

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
import io.micrometer.observation.annotation.Observed
import lombok.extern.slf4j.Slf4j
import org.hibernate.query.sqm.tree.SqmNode.log

@RestController
@Slf4j
@Observed
class TicketingController(private val ticketService: TicketService) {
    @GetMapping("/tickets/")
    @ResponseStatus(HttpStatus.OK)
    @Secured("MANAGER", "EXPERT", "PROFILE")
    fun getAll(): List<TicketDTO>{
        return ticketService.getAll()
    } // MANAGER ONLY -> CHECK USEFULNESS

    @GetMapping("/tickets/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("MANAGER", "EXPERT", "PROFILE")
    fun getTicketByID(@PathVariable ticketId:Long) : TicketDTO {
        return ticketService.getTicketByID(ticketId)
            ?: throw TicketNotFoundException("Ticket not found")
    } // MANAGER ONLY -> CHECK USEFULNESS
      // MANAGER ONLY -> IMPLEMENT GET TICKETS BY ARG

    @PostMapping("/tickets/")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    @Secured("PROFILE")
    fun createTicket(@RequestBody ticket: TicketCreateBodyDTO?){
        if (ticket === null) {
            throw NoBodyProvidedException("You have to add a body")
        }
        ticketService.insertTicket(ticket)
        // ADD SERVICE IMPLEMENTATION FOR THE FIRST MESSAGE TO BE LINKED
    }

    @PutMapping("/tickets/{ticketId}/open")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("PROFILE")
    fun openTicket(@PathVariable ticketId:Long){
        ticketService.setTicketStatus(
            ticketId,
            Status.OPEN,
            StatusChanger.PROFILE,
            null,
            null
        )
    } // MANAGER ONLY -> SERVICE IMPLEMENTATION TO BE CHECKED

    @PutMapping("/tickets/{ticketId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("MANAGER", "EXPERT", "PROFILE")
    @Transactional
    fun closeTicket(principal: Principal, @PathVariable ticketId:Long){
        val token = principal as JwtAuthenticationToken

        val statusChanger = StatusChanger.values()
            .firstOrNull { sc -> token.authorities.map { it.authority }.contains(sc.name) }
        if (statusChanger === null) {
            throw IllegalStatusChangeException("")
        }
        ticketService.setTicketStatus(
            ticketId,
            Status.CLOSED,
            statusChanger,
            null,
            null)
    }// TO BE DISCUSSED

    @PutMapping("/tickets/{ticketId}/reopen")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("PROFILE")
    @Transactional
    fun reOpenTicket(@PathVariable ticketId:Long){
        ticketService.setTicketStatus(
            ticketId,
            Status.REOPENED,
            StatusChanger.PROFILE,
            null,
            null
        )
    }

    @PutMapping("/tickets/{ticketId}/resolved")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("PROFILE")
    @Transactional
    fun resolveTicket(@PathVariable ticketId:Long){
        ticketService.setTicketStatus(
            ticketId,
            Status.RESOLVED,
            StatusChanger.PROFILE,
            null,
            null
        )
    } // TO BE DISCUSSED

    @PutMapping("/tickets/{ticketId}/inprogress")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("MANAGER")
    @Transactional
    fun inProgressTicket(@PathVariable ticketId:Long, @RequestBody body: TicketInProgressBodyDTO?){
        if (body === null) {
            throw NoBodyProvidedException("Wrong body")
        }
        ticketService.setTicketStatus(
            ticketId,
            Status.IN_PROGRESS,
            StatusChanger.MANAGER,
            body.expert,
            body.priority
        )
    }

    @PutMapping("/tickets/{ticketId}/set_priority/{priority}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("MANAGER")
    fun setTicketPriority(@PathVariable ticketId:Long, @PathVariable priority: String){
        ticketService.setTicketPriority(ticketId, priority)
    }
}