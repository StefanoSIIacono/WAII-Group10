package com.lab2.server.ticketing

interface TicketService {
    fun getAll(): List<TicketDTO>
    fun getTicketByID(ticketId: Long): TicketDTO?
    fun insertTicket(ticket: TicketDTO)
}