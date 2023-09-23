package com.lab2.server.services

import com.lab2.server.data.Priority
import com.lab2.server.data.Status
import com.lab2.server.data.Ticket
import com.lab2.server.dto.PagedDTO
import com.lab2.server.dto.TicketCreateBodyDTO
import com.lab2.server.dto.TicketDTO
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

interface TicketService {
    fun getAllPaginated(page: Int, offset: Int, user: JwtAuthenticationToken): PagedDTO<TicketDTO>
    fun getTicketByID(ticketId: Long, user: JwtAuthenticationToken): TicketDTO

    fun getTicketsByEmailPaginated(
        email: String,
        page: Int,
        offset: Int,
        user: JwtAuthenticationToken
    ): PagedDTO<TicketDTO>

    fun insertTicket(ticket: TicketCreateBodyDTO, user: JwtAuthenticationToken)
    fun setTicketStatus(
        user: JwtAuthenticationToken,
        ticketId: Long,
        inputStatus: Status,
        expertEmail: String? = null,
        priority: Priority? = null
    )

    fun setTicketPriority(ticketId: Long, priority: Priority)

    fun unsafeGetTicketByID(ticketId: Long): Ticket?
    fun unsafeTicketSave(ticket: Ticket)
}