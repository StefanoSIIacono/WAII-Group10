package com.lab2.server.serviceImpl

import com.lab2.server.data.Message
import com.lab2.server.data.Roles
import com.lab2.server.dto.*
import com.lab2.server.exceptionsHandler.exceptions.AckMessageInTheFutureException
import com.lab2.server.exceptionsHandler.exceptions.TicketNotFoundException
import com.lab2.server.repositories.MessageRepository
import com.lab2.server.services.MessageService
import com.lab2.server.services.TicketService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import java.util.*

@Service
class MessageServiceImpl(
    private val ticketingService: TicketService,
    private val messageRepository: MessageRepository
) : MessageService {
    override fun handleNewMessage(sender: JwtAuthenticationToken, ticketId: Long, messageDTO: BodyMessageDTO) {
        val ticket = ticketingService.unsafeGetTicketByID(ticketId)
            ?: throw TicketNotFoundException("Ticket not found")

        if (ticket.expert?.email != sender.name && ticket.profile.email != sender.name) {
            throw TicketNotFoundException("No ticket found")
        }

        val lastTimestamp = messageRepository.findFirstByTicketIdOrderByTimestampDesc(ticketId)?.timestamp

        var currentTimestamp = Date(System.currentTimeMillis())

        if (lastTimestamp != null && currentTimestamp <= lastTimestamp) {
            currentTimestamp = Date(lastTimestamp.time.inc())
        }

        val message = Message(currentTimestamp, messageDTO.body)

        if (ticket.expert?.email == sender.name) {
            ticket.addMessageFromExpert(message)
        } else {
            ticket.addMessageFromProfile(message)
        }

        ticketingService.unsafeTicketSave(ticket)
    }

    override fun acknowledgeMessage(ticketID: Long, user: JwtAuthenticationToken, ack: MessageReadAck) {
        val ticket = ticketingService.unsafeGetTicketByID(ticketID)
            ?: throw TicketNotFoundException("Ticket not found")

        if (ticket.messages.size < ack.id) {
            throw AckMessageInTheFutureException("Id doesn't exist")
        }

        if (ticket.expert?.email == user.name) {
            ticket.updateLastReadExpert(ack.id)
            return
        }
        if (ticket.profile.email == user.name) {
            ticket.updateLastReadProfile(ack.id)
            return
        }

        throw TicketNotFoundException("No ticket found")
    }

    override fun getTicketPagedMessages(
        ticketID: Long,
        user: JwtAuthenticationToken,
        page: Int,
        offset: Int
    ): PagedDTO<MessageDTO> {
        val ticket = ticketingService.unsafeGetTicketByID(ticketID)
            ?: throw TicketNotFoundException("Ticket not found")

        val userRole = Roles.values()
            .firstOrNull { sc -> user.authorities.map { it.authority }.contains(sc.name) }
        if (ticket.expert?.email != user.name && ticket.profile.email != user.name && userRole !== Roles.MANAGER) {
            throw TicketNotFoundException("No ticket found")
        }

        val pageResult =
            messageRepository.findAll(PageRequest.of(page, offset, Sort.by(Sort.Direction.DESC, "timestamp")))
        val meta = PagedMetadata(pageResult.number, pageResult.totalPages, pageResult.numberOfElements)

        return PagedDTO(meta, pageResult.toList().map { it.toDTO() })
    }
}