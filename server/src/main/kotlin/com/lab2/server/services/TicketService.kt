package com.lab2.server.services

import com.lab2.server.data.Priority
import com.lab2.server.data.Status
import com.lab2.server.data.StatusChanger
import com.lab2.server.dto.TicketCreateBodyDTO
import com.lab2.server.dto.TicketDTO

interface TicketService {
    fun getAll(): List<TicketDTO>
    fun getTicketByID(ticketId: Long): TicketDTO?
    fun insertTicket(ticket: TicketDTO)
    fun setTicketStatus(ticketId: Long, inputStatus: Status, expertId: Long?, statusChanger: StatusChanger)
    fun setTicketPriority(ticketId: Long, priority: Priority)
}