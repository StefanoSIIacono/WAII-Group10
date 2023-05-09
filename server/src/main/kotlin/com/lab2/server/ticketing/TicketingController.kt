package com.lab2.server.ticketing

import com.lab2.server.exceptionsHandler.exceptions.TicketNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class TicketingController(private val ticketService: TicketService) {
    @GetMapping("/tickets/")
    @ResponseStatus(HttpStatus.OK)
    fun getAll(): List<TicketDTO>{
        return ticketService.getAll()
    }

    @GetMapping("tickets/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getTicketByID(@PathVariable ticketId:Long) : TicketDTO{
        return ticketService.getTicketByID(ticketId)
            ?: throw TicketNotFoundException("Ticket not found")
    }

    @PostMapping("tickets/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createTicket(@RequestBody ticket: TicketDTO?){
        if(ticket!=null)
            ticketService.insertTicket(ticket)
    }

}