package com.lab2.server.serviceImpl

import com.lab2.server.data.*
import com.lab2.server.dto.TicketCreateBodyDTO
import com.lab2.server.dto.TicketDTO
import com.lab2.server.dto.toDTO
import com.lab2.server.exceptionsHandler.exceptions.*
import com.lab2.server.repositories.ProductRepository
import com.lab2.server.repositories.ProfileRepository
import com.lab2.server.repositories.TicketingRepository
import com.lab2.server.services.ExpertService
import com.lab2.server.services.ProductService
import com.lab2.server.services.ProfileService
import com.lab2.server.services.TicketService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class TicketServiceImpl (private val ticketingRepository: TicketingRepository, private val profileService: ProfileService, private val productService: ProductService, private val expertService: ExpertService):
    TicketService {

    override fun getAll(): List<TicketDTO> {
        return ticketingRepository.findAll().map{ it.toDTO() }
    }

    override fun getTicketByID(ticketId: Long): TicketDTO? {
        return ticketingRepository.findByIdOrNull(ticketId)?.toDTO() ?: throw TicketNotFoundException("Ticket not found")
    }

    override fun insertTicket(ticket: TicketDTO) {
        val profile = profileService.getProfileByEmail(ticket.profile.email) ?: throw ProfileNotFoundException("Profile not found")

        val product = productService.getProduct(ticket.product.productId) ?: throw ProductNotFoundException("Product not found")

        val newTicket = Ticket(ticket.obj, ticket.arg, Priority.TOASSIGN, profile.toProfile(), null, product.toProduct())
        val status = TicketStatus(Status.OPEN, Date(System.currentTimeMillis()), newTicket)

        newTicket.addStatus(status)
        ticketingRepository.save(newTicket)
    }

    override fun setTicketStatus(ticketId: Long, inputStatus: Status, expertId: Long?, statusChanger: StatusChanger) {
        val ticket = ticketingRepository.findByIdOrNull(ticketId) ?: throw TicketNotFoundException("Ticket not found")

        val currentStatus = ticket.statusHistory.maxBy { it.timestamp }

        // IF expertId === null, THE STATUS IS CHANGED BY THE PROFILE WHEN THE TICKET IS OPENED/REOPENED
        val expert = if (expertId === null) null else expertService.getExpertById(expertId)?.toExpert() ?: throw ExpertNotFoundException("Expert not found")

        if (!(validStatusChanges[currentStatus.status]!!.contains(inputStatus))) {
            throw IllegalStatusChangeException("can't go from ${currentStatus.status} to $inputStatus")
        }

        if (expert === null && inputStatus === Status.IN_PROGRESS) {
            throw ExpertNotFoundException("Expert not found")
        }

        /*if ((priority === null || priority === Priority.TOASSIGN) && inputStatus === Status.IN_PROGRESS) {
            throw IllegalPriorityException("Priority not valid")
        }*/

        val status = TicketStatus(
            inputStatus, Date(System.currentTimeMillis()), ticket, statusChanger,
                if (inputStatus === Status.IN_PROGRESS) expert else null
        )

        if (inputStatus === Status.IN_PROGRESS && expert != null)
            expert.addTicketStatus(status)

        ticket.addStatus(status)
        /*if (inputStatus === Status.IN_PROGRESS) {
            ticket.newPriority(priority!!)
        }*/

        ticketingRepository.save(ticket)
    }
}