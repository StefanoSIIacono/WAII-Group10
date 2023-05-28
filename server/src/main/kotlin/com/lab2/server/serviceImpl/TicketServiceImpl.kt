package com.lab2.server.serviceImpl

import com.lab2.server.data.*
import com.lab2.server.dto.TicketCreateBodyDTO
import com.lab2.server.dto.TicketDTO
import com.lab2.server.dto.toDTO
import com.lab2.server.exceptionsHandler.exceptions.*
import com.lab2.server.repositories.TicketingRepository
import com.lab2.server.services.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class TicketServiceImpl (private val ticketingRepository: TicketingRepository, private val profileService: ProfileService,
                         private val productService: ProductService, private val expertService: ExpertService, private val expertiseService :ExpertiseService):
    TicketService {

    override fun getAll(): List<TicketDTO> {
        return ticketingRepository.findAll().map{ it.toDTO() }
    }

    override fun getTicketByID(ticketId: Long): TicketDTO? {
        return ticketingRepository.findByIdOrNull(ticketId)?.toDTO() ?: throw TicketNotFoundException("Ticket not found")
    }

    override fun insertTicket(ticket: TicketCreateBodyDTO) {
        val profile = profileService.provideProfileByEmail(ticket.profile) ?: throw ProfileNotFoundException("Profile not found")

        val product = productService.getProduct(ticket.product) ?: throw ProductNotFoundException("Product not found")

        val newTicket = Ticket(ticket.obj, expertiseService.getExpertise(ticket.arg)!!.toExpertise(), Priority.TOASSIGN, profile, null, product.toProduct())
        val status = TicketStatus(Status.OPEN, Date(System.currentTimeMillis()), newTicket)

        newTicket.addStatus(status)
        ticketingRepository.save(newTicket)
    }

    override fun setTicketStatus(ticketId: Long, inputStatus: Status, statusChanger: StatusChanger, expertEmail: String?, priority: Priority?) {
        val ticket = ticketingRepository.findByIdOrNull(ticketId)
            ?: throw TicketNotFoundException("Ticket not found")

        val currentStatus = ticket.statusHistory.maxBy { it.timestamp }

        // IF expertId === null, THE STATUS IS CHANGED BY THE PROFILE WHEN THE TICKET IS OPENED/REOPENED
        val expert = if (expertEmail === null) null
                else (expertService.getExpertByEmail(expertEmail)?.toExpert()
            ?: throw ExpertNotFoundException("Expert not found"))

        if (!(validStatusChanges[currentStatus.status]!!.contains(inputStatus))) {
            throw IllegalStatusChangeException("can't go from ${currentStatus.status} to $inputStatus")
        }

        if (expert === null && inputStatus === Status.IN_PROGRESS) {
            throw ExpertIsNullException("Expert cannot be null when status is in progress")
        }

        if ((priority === null || priority === Priority.TOASSIGN) && inputStatus === Status.IN_PROGRESS) {
            throw IllegalPriorityException("Priority not valid")
        }

        val status = TicketStatus(
            inputStatus, Date(System.currentTimeMillis()), ticket, statusChanger,
                if (inputStatus === Status.IN_PROGRESS) expert else null
        )

        ticket.addStatus(status)
        if (inputStatus === Status.IN_PROGRESS) {
            expert!!.addTicketStatus(status)
            this.setTicketPriority(ticketId, priority!!.toString())
            expert.addTicket(ticket)
            expertService.addTicketToExpert(expert, ticket)
        } else if (ticket.expert != null){
            ticket.removeExpert()
        }

        ticketingRepository.save(ticket)
    }

    override fun setTicketPriority(ticketId: Long, priority: String) {
        val ticket = ticketingRepository.findByIdOrNull(ticketId) ?: throw TicketNotFoundException("Ticket not found")

        if (priority.uppercase() == "HIGH")
            ticket.newPriority(Priority.HIGH)
        else if (priority.uppercase() == "MEDIUM")
            ticket.newPriority(Priority.MEDIUM)
        else if (priority.uppercase() == "LOW")
            ticket.newPriority(Priority.LOW)
        else if (priority.uppercase() == "TOASSIGN")
            ticket.newPriority(Priority.TOASSIGN)
        else throw IllegalPriorityException("Priority is illegal")

        ticketingRepository.save(ticket)
    }


}