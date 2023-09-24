package com.lab2.server.serviceImpl

import com.lab2.server.data.*
import com.lab2.server.dto.*
import com.lab2.server.exceptionsHandler.exceptions.*
import com.lab2.server.repositories.TicketingRepository
import com.lab2.server.services.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import java.util.*
import javax.ws.rs.NotAuthorizedException
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

@Service
class TicketServiceImpl(
    private val ticketingRepository: TicketingRepository,
    private val profileService: ProfileService,
    private val productService: ProductService,
    private val expertService: ExpertService,
    private val expertiseService: ExpertiseService,
) :
    TicketService {

    override fun getAllPaginated(
        page: Int,
        offset: Int,
        user: JwtAuthenticationToken
    ): PagedDTO<TicketDTO> {
        val userRole = Roles.values()
            .firstOrNull { sc -> user.authorities.map { it.authority }.contains(sc.name) }

        val result: List<Ticket> = if (userRole === Roles.MANAGER) {
            ticketingRepository.findAll()
        } else if (userRole === Roles.EXPERT) {
            ticketingRepository.findAllByExpertEmail(user.name)
        } else if (userRole === Roles.PROFILE) {
            ticketingRepository.findAllByProfileEmail(user.name)
        } else {
            throw NotAuthorizedException("Not authorized")
        }

        val sortedResult = result.sortedWith(compareBy<Ticket> { it.statusHistory.last().status }.thenBy { it.priority }
            .thenBy { it.statusHistory.last().timestamp })

        val numberOfPages = ceil(sortedResult.size.toDouble() / offset).toInt()
        val numberOfElements = min(offset, max(0, sortedResult.size - offset * page))

        val meta = PagedMetadata(min(page + 1, numberOfPages), numberOfPages, numberOfElements)

        return PagedDTO(
            meta,
            sortedResult.subList(page * offset, page * offset + numberOfElements).map { it.toDTO(user.name) })
    }

    override fun getTicketByID(ticketId: Long, user: JwtAuthenticationToken): TicketDTO {
        val ticket = ticketingRepository.findByIdOrNull(ticketId)
        if (ticket === null) {
            throw TicketNotFoundException("Ticket not found")
        }

        val userRole = Roles.values()
            .firstOrNull { sc -> user.authorities.map { it.authority }.contains(sc.name) }

        if (ticket.profile.email != user.name && ticket.expert?.email != user.name && userRole !== Roles.MANAGER) {
            throw TicketNotFoundException("Ticket not found")
        }

        return ticket.toDTO(user.name)
    }

    override fun getTicketsByEmailPaginated(
        email: String,
        page: Int,
        offset: Int,
        user: JwtAuthenticationToken
    ): PagedDTO<TicketDTO> {
        val profile = profileService.unsafeProfileByEmail(email)

        if (profile === null) {
            throw ProfileNotFoundException("Profile not found")
        }

        val userRole = Roles.values()
            .firstOrNull { sc -> user.authorities.map { it.authority }.contains(sc.name) }
        if (email != user.name && userRole != Roles.MANAGER) {
            throw NotAuthorizedException("Not allowed")
        }

        val pageResult = ticketingRepository.findAllByProfileEmail(profile.email, PageRequest.of(page, offset))

        val meta = PagedMetadata(pageResult.number + 1, pageResult.totalPages, pageResult.numberOfElements)

        return PagedDTO(meta, pageResult.content.map { it.toDTO() })
    }

    override fun insertTicket(ticket: TicketCreateBodyDTO, user: JwtAuthenticationToken) {
        val product = productService.getProduct(ticket.product)
        val profile = profileService.unsafeProfileByEmail(user.name)!!
        val expertise = expertiseService.getExpertise(ticket.arg)

        val newTicket =
            Ticket(ticket.obj, expertise.toExpertise(), Priority.TOASSIGN, profile, null, product.toProduct())
        val currentTime = Date(System.currentTimeMillis())
        val status = TicketStatus(Status.OPEN, currentTime)
        val message = Message(0, currentTime, ticket.body)

        if (ticket.attachments.size > 0) {
            ticket.attachments.forEach {
                try {
                    Base64.getDecoder().decode(it.attachment)
                } catch (e: IllegalArgumentException) {
                    throw InvalidBase64Exception("Invalid base64")
                }
            }
            message.addAttachments(ticket.attachments.map {
                Attachment(
                    it.attachment,
                    it.attachment.length.toLong(),
                    it.contentType
                )
            })
        }

        newTicket.addMessageFromProfile(message)
        newTicket.addStatus(status, Roles.PROFILE)

        ticketingRepository.save(newTicket)
    }

    override fun setTicketStatus(
        user: JwtAuthenticationToken,
        ticketId: Long,
        inputStatus: Status,
        expertEmail: String?,
        priority: Priority?
    ) {
        val ticket = ticketingRepository.findByIdOrNull(ticketId)
            ?: throw TicketNotFoundException("Ticket not found")

        val userRole = Roles.values()
            .firstOrNull { sc -> user.authorities.map { it.authority }.contains(sc.name) }!!
        if (ticket.profile.email != user.name && ticket.expert?.email != user.name && userRole !== Roles.MANAGER) {
            throw TicketNotFoundException("Ticket not found")
        }

        val currentStatus = ticket.statusHistory.maxBy { it.timestamp }

        // IF expertId === null, THE STATUS IS CHANGED BY THE PROFILE WHEN THE TICKET IS OPENED/REOPENED
        val expert = if (expertEmail === null) null
        else (expertService.unsafeGetExpertByEmail(expertEmail)
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
            inputStatus, Date(System.currentTimeMillis())
        )

        ticket.addStatus(status, userRole, if (inputStatus === Status.IN_PROGRESS) expert else null)
        if (inputStatus === Status.IN_PROGRESS) {
            ticket.changePriority(priority!!)
        }

        ticketingRepository.save(ticket)
    }

    override fun setTicketPriority(ticketId: Long, priority: Priority) {
        val ticket = ticketingRepository.findByIdOrNull(ticketId) ?: throw TicketNotFoundException("Ticket not found")
        val currentStatus = ticket.statusHistory.maxBy { it.timestamp }
        if (currentStatus.status !== Status.IN_PROGRESS) {
            throw IllegalPriorityException("Priority can be changed only while in progress")
        }
        ticket.changePriority(priority)

        ticketingRepository.save(ticket)
    }


    override fun unsafeGetTicketByID(ticketId: Long): Ticket? {
        return ticketingRepository.findByIdOrNull(ticketId)
    }

    override fun unsafeTicketSave(ticket: Ticket) {
        ticketingRepository.save(ticket)
    }
}