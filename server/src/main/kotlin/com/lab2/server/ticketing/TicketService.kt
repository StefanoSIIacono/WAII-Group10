package com.lab2.server.ticketing

interface TicketService {
    fun getAll(): List<TicketDTO>
    fun getTicketByID(ticketId: Long): TicketDTO?
    fun insertTicket(ticket: TicketCreateBodyDTO)

    fun setTicketToOpen(ticketId: Long)
    fun setTicketToClosed(ticketId: Long)
    fun setTicketToReopen(ticketId: Long)
    fun setTicketToResolved(ticketId: Long)
    fun setTicketToInProgress(ticketId: Long, expert: Long)

}