package com.lab2.server.services

import com.lab2.server.dto.TicketCreateBodyDTO
import com.lab2.server.dto.TicketDTO

interface TicketService {
    fun getAll(): List<TicketDTO>
    fun getTicketByID(ticketId: Long): TicketDTO?
    fun insertTicket(ticket: TicketCreateBodyDTO)
    fun setTicketStatus(ticketId: Long, status: String)
    fun setTicketToOpen(ticketId: Long)
    fun setTicketToClosed(ticketId: Long)
    fun setTicketToReopen(ticketId: Long)
    fun setTicketToResolved(ticketId: Long)
    fun setTicketToInProgress(ticketId: Long, expert: Long)

}