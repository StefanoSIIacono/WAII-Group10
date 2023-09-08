package com.lab2.server.services

import com.lab2.server.data.Priority
import com.lab2.server.data.Status
import com.lab2.server.data.StatusChanger
import com.lab2.server.dto.TicketCreateBodyDTO
import com.lab2.server.dto.TicketDTO

interface TicketService {
    fun getAll(loggedInUser: String): List<TicketDTO>
    fun getTicketByID(ticketId: Long, loggedInUser: String): TicketDTO?
    fun insertTicket(ticket: TicketCreateBodyDTO)
    fun setTicketStatus(ticketId: Long, inputStatus: Status, statusChanger: StatusChanger, expertEmail: String?, priority: Priority?)
    fun setTicketPriority(ticketId: Long, priority: String)
}