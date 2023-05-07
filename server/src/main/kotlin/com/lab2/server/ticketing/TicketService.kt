package com.lab2.server.ticketing

interface TicketService {
    fun getAll(): List<TicketDTO>
}