package com.lab2.server.controllers

import com.lab2.server.dto.TicketCreateBodyDTO
import com.lab2.server.dto.TicketDTO
import com.lab2.server.dto.TicketInProgressBodyDTO
import com.lab2.server.exceptionsHandler.exceptions.TicketNotFoundException
import com.lab2.server.services.TicketService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class TicketingController(private val ticketService: TicketService) {
    @GetMapping("/tickets/")
    @ResponseStatus(HttpStatus.OK)
    fun getAll(): List<TicketDTO>{
        return ticketService.getAll()
    }

    @GetMapping("tickets/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun getTicketByID(@PathVariable ticketId:Long) : TicketDTO {
        return ticketService.getTicketByID(ticketId)
            ?: throw TicketNotFoundException("Ticket not found")
    }

    @PostMapping("tickets/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createTicket(@RequestBody ticket: TicketCreateBodyDTO?){
        if(ticket!=null)
            ticketService.insertTicket(ticket)
    }

    @PutMapping("tickets/{ticketId}/open")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun openTicket(@PathVariable ticketId:Long){
        ticketService.setTicketToOpen(ticketId)
    }

    @PutMapping("tickets/{ticketId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun closeTicket(@PathVariable ticketId:Long){
        ticketService.setTicketToClosed(ticketId)
    }

    @PutMapping("tickets/{ticketId}/reopen")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun reopenTicket(@PathVariable ticketId:Long){
        ticketService.setTicketToReopen(ticketId)
    }

    @PutMapping("tickets/{ticketId}/resolved")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun resolveTicket(@PathVariable ticketId:Long){
        ticketService.setTicketToResolved(ticketId)
    }

    @PutMapping("tickets/{ticketId}/inprogress")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun progressTicket(@PathVariable ticketId:Long, @RequestBody body: TicketInProgressBodyDTO?){
        if (body !== null) {
            ticketService.setTicketToInProgress(ticketId, body.expert)
        }
    }


}