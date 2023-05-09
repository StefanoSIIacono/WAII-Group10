package com.lab2.server.ticketing

import com.lab2.server.profiles.ProfileRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TicketServiceImpl (private val ticketingRepository: TicketingRepository): TicketService  {

    override fun getAll(): List<TicketDTO> {
        return ticketingRepository.findAll().map{ it.toDTO() }

    }

    override fun getTicketByID(ticketId: Long): TicketDTO? {
        return ticketingRepository.findByIdOrNull(ticketId)?.toDTO()
    }

    override fun insertTicket(ticket: TicketDTO) {
        ticketingRepository.save(ticket.toTicket())
    }

}