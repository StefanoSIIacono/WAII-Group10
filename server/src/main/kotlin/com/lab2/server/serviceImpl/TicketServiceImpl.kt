package com.lab2.server.serviceImpl

import com.lab2.server.dto.TicketCreateBodyDTO
import com.lab2.server.dto.TicketDTO
import com.lab2.server.dto.toDTO
import com.lab2.server.data.Priority
import com.lab2.server.data.Status
import com.lab2.server.data.Ticket
import com.lab2.server.data.TicketStatus
import com.lab2.server.exceptionsHandler.exceptions.*
import com.lab2.server.repositories.ProductRepository
import com.lab2.server.repositories.ProfileRepository
import com.lab2.server.repositories.TicketingRepository
import com.lab2.server.services.TicketService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class TicketServiceImpl (private val ticketingRepository: TicketingRepository, private val profileRepository: ProfileRepository, private val productRepository: ProductRepository):
    TicketService {

    override fun getAll(): List<TicketDTO> {
        return ticketingRepository.findAll().map{ it.toDTO() }

    }

    override fun getTicketByID(ticketId: Long): TicketDTO? {
        return ticketingRepository.findByIdOrNull(ticketId)?.toDTO() ?: throw TicketNotFoundException("Ticket not found")
    }

    override fun insertTicket(ticket: TicketCreateBodyDTO) {
        val profile = profileRepository.findByIdOrNull(ticket.profile) ?: throw ProfileNotFoundException("Profile not found")

        val product = productRepository.findByIdOrNull(ticket.product) ?: throw ProductNotFoundException("Product not found")

        val newTicket = Ticket(ticket.obj, ticket.arg, Priority.TOASSIGN, profile, null, product)
        val status = TicketStatus(Status.OPEN, Date(System.currentTimeMillis()), "unknown", newTicket)
        // newTicket.statusHistory.add(status)
        newTicket.addStatus(status)
        ticketingRepository.save(newTicket)
    }

    override fun setTicketStatus(ticketId: Long, status: String) {
        TODO("Not yet implemented")
    }

    override fun setTicketToOpen(ticketId: Long) {
        val ticket = ticketingRepository.findByIdOrNull(ticketId) ?: throw TicketNotFoundException("Ticket not found")

        val currentStatus = ticket.statusHistory.maxBy { it.timestamp }

        if (currentStatus.status !== Status.IN_PROGRESS) {
            throw IllegalStatusChangeException("can't go from ${currentStatus.status} to OPEN")
        }

        val status = TicketStatus(Status.OPEN, Date(System.currentTimeMillis()), "unknown", ticket)
        ticket.addStatus(status)
        ticketingRepository.save(ticket)
    }
    override fun setTicketToClosed(ticketId: Long) {
        val ticket = ticketingRepository.findByIdOrNull(ticketId) ?: throw TicketNotFoundException("Ticket not found")

        val currentStatus = ticket.statusHistory.maxBy { it.timestamp }

        if (currentStatus.status === Status.CLOSED) {
            throw IllegalStatusChangeException("can't go from ${currentStatus.status} to CLOSED")
        }

        val status = TicketStatus(Status.CLOSED, Date(System.currentTimeMillis()), "unknown", ticket)
        ticket.addStatus(status)
        ticketingRepository.save(ticket)
    }
    override fun setTicketToReopen(ticketId: Long) {
        val ticket = ticketingRepository.findByIdOrNull(ticketId) ?: throw TicketNotFoundException("Ticket not found")

        val currentStatus = ticket.statusHistory.maxBy { it.timestamp }

        if (arrayOf(Status.OPEN, Status.IN_PROGRESS, Status.REOPENED).contains(currentStatus.status)) {
            throw IllegalStatusChangeException("can't go from ${currentStatus.status} to REOPEN")
        }

        val status = TicketStatus(Status.REOPENED, Date(System.currentTimeMillis()), "unknown", ticket)
        ticket.addStatus(status)
        ticketingRepository.save(ticket)
    }
    override fun setTicketToResolved(ticketId: Long) {
        val ticket = ticketingRepository.findByIdOrNull(ticketId) ?: throw TicketNotFoundException("Ticket not found")

        val currentStatus = ticket.statusHistory.maxBy { it.timestamp }

        if (arrayOf(Status.CLOSED, Status.RESOLVED).contains(currentStatus.status)) {
            throw IllegalStatusChangeException("can't go from ${currentStatus.status} to RESOLVED")
        }

        val status = TicketStatus(Status.RESOLVED, Date(System.currentTimeMillis()), "unknown", ticket)
        ticket.addStatus(status)
        ticketingRepository.save(ticket)
    }
    override fun setTicketToInProgress(ticketId: Long, expert: Long) {
        val ticket = ticketingRepository.findByIdOrNull(ticketId) ?: throw TicketNotFoundException("Ticket not found")

        val currentStatus = ticket.statusHistory.maxBy { it.timestamp }

        if (arrayOf(Status.CLOSED, Status.RESOLVED, Status.IN_PROGRESS).contains(currentStatus.status)) {
            throw IllegalStatusChangeException("can't go from ${currentStatus.status} to IN PROGRESS")
        }
        // TODO: set expert but i don't know how
        val status = TicketStatus(Status.IN_PROGRESS, Date(System.currentTimeMillis()), "unknown", ticket)
        ticket.addStatus(status)
        ticketingRepository.save(ticket)
    }

}