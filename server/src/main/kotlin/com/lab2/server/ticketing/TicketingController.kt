package com.lab2.server.ticketing

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class TicketingController(private val ticketService: TicketService) {
    @GetMapping("/tickets/")
    @ResponseStatus(HttpStatus.OK)
    fun getAll(): List<TicketDTO>{
        return ticketService.getAll()
    }
}