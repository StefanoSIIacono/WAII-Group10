package com.lab2.server.serviceImpl

import com.lab2.server.data.Attachment
import com.lab2.server.data.Message
import com.lab2.server.data.Roles
import com.lab2.server.dto.*
import com.lab2.server.exceptionsHandler.exceptions.AckMessageInTheFutureException
import com.lab2.server.exceptionsHandler.exceptions.InvalidBase64Exception
import com.lab2.server.exceptionsHandler.exceptions.TicketNotFoundException
import com.lab2.server.repositories.MessageRepository
import com.lab2.server.services.MessageService
import com.lab2.server.services.TicketService
import org.springframework.data.domain.PageRequest
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

        val message = Message(
            ticket.messages.last().index + 1,
            currentTimestamp,
            messageDTO.body,
        )

        if (messageDTO.attachments.size > 0) {
            messageDTO.attachments.forEach {
                try {
                    Base64.getDecoder().decode(it.attachment)
                } catch (e: IllegalArgumentException) {
                    throw InvalidBase64Exception("Invalid base64")
                }
            }
            message.addAttachments(messageDTO.attachments.map {
                Attachment(
                    it.attachment,
                    it.attachment.length.toLong(),
                    it.contentType
                )
            })
        }

        if (ticket.expert?.email == sender.name) {
            ticket.addMessageFromExpert(message)
        } else {
            ticket.addMessageFromProfile(message)
        }

        ticketingService.unsafeTicketSave(ticket)
    }

    override fun acknowledgeMessage(ticketID: Long, user: JwtAuthenticationToken, ack: Int) {
        val ticket = ticketingService.unsafeGetTicketByID(ticketID)
            ?: throw TicketNotFoundException("Ticket not found")

        if (ticket.messages.size < ack) {
            throw AckMessageInTheFutureException("Id doesn't exist")
        }

        if (ticket.expert?.email == user.name) {
            ticket.updateLastReadExpert(ack)
            ticketingService.unsafeTicketSave(ticket)
            return
        }
        if (ticket.profile.email == user.name) {
            ticket.updateLastReadProfile(ack)
            ticketingService.unsafeTicketSave(ticket)
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

        val pageResult = messageRepository.findByTicketIdOrderByTimestampDesc(ticketID, PageRequest.of(page, offset))

        val meta = PagedMetadata(pageResult.number + 1, pageResult.totalPages, pageResult.numberOfElements)
        return PagedDTO(meta, pageResult.content.map { it.toDTO() })
    }

    override fun getUnreadMessages(
        user: JwtAuthenticationToken,
        page: Int,
        offset: Int
    ): PagedDTO<UnreadMessagesDTO> {
        val userRole = Roles.values()
            .firstOrNull { sc -> user.authorities.map { it.authority }.contains(sc.name) }
        val pageResult = if (userRole == Roles.PROFILE) messageRepository.findAllProfileUnreadMessages(
            user.name,
            PageRequest.of(page, offset)
        ) else messageRepository.findAllExpertUnreadMessages(user.name, PageRequest.of(page, offset))

        val meta = PagedMetadata(pageResult.number + 1, pageResult.totalPages, pageResult.numberOfElements)

        return PagedDTO(
            meta,
            pageResult.content.map { ticket ->
                UnreadMessagesDTO(
                    ticket.id!!,
                    if (userRole == Roles.PROFILE) ticket.lastReadMessageIndexProfile else ticket.lastReadMessageIndexExpert,
                    if (userRole == Roles.PROFILE) ticket.messages.maxOf { it.index } - ticket.lastReadMessageIndexProfile else ticket.messages.maxOf { it.index } - ticket.lastReadMessageIndexExpert
                )
            })
    }
}