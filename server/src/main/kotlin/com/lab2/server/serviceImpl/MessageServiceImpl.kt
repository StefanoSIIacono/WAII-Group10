package com.lab2.server.serviceImpl

import com.lab2.server.data.Message
import com.lab2.server.dto.*
import com.lab2.server.exceptionsHandler.exceptions.AckMessageInTheFutureException
import com.lab2.server.exceptionsHandler.exceptions.TicketNotFoundException
import com.lab2.server.repositories.MessageRepository
import com.lab2.server.repositories.TicketingRepository
import com.lab2.server.services.MessageService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*
@Service
class MessageServiceImpl(private val ticketingRepository: TicketingRepository, private val messageRepository: MessageRepository): MessageService {
    override fun handleNewMessage(sender: String, ticketId: Long, messageDTO: BodyMessageDTO) {
        val ticket = ticketingRepository.findByIdOrNull(ticketId)
            ?: throw TicketNotFoundException("Ticket not found")

        if (ticket.expert?.email != sender && ticket.profile.email != sender) {
            throw TicketNotFoundException("No ticket found")
        }

        val lastTimestamp = messageRepository.findFirstByTicketIdOrderByTimestampDesc(ticketId)?.timestamp

        var currentTimestamp = Date(System.currentTimeMillis())

        if (lastTimestamp != null && currentTimestamp <= lastTimestamp) {
           currentTimestamp = Date(lastTimestamp.time.inc())
        }

        val message = Message(currentTimestamp, messageDTO.body, if (ticket.expert?.email == sender) ticket.expert else null)

        ticket.addMessage(message)
        messageRepository.save(message)
        ticketingRepository.save(ticket)
    }

    override fun acknowledgeMessage(ticketID: Long, user: String, ack: MessageReadAck) {
        val ticket = ticketingRepository.findByIdOrNull(ticketID)
            ?: throw TicketNotFoundException("Ticket not found")

        if (ticket.messages.size < ack.id) {
            throw AckMessageInTheFutureException("Id doesn't exist")
        }

        if (ticket.expert?.email == user){
            ticket.lastReadMessageIndexExpert = ack.id
            return
        }
        if (ticket.profile.email == user){
            ticket.lastReadMessageIndexProfile = ack.id
            return
        }

        throw TicketNotFoundException("No ticket found")
    }

    override fun getTicketPagedMessages(ticketID: Long, user: String, page: Int, offset: Int): PagedDTO<MessageDTO> {
        val ticket = ticketingRepository.findByIdOrNull(ticketID)
            ?: throw TicketNotFoundException("Ticket not found")

        if (ticket.expert?.email != user && ticket.profile.email != user) {
            throw TicketNotFoundException("No ticket found")
        }

        val pageResult = messageRepository.findAll(PageRequest.of(page, offset, Sort.by(Sort.Direction.DESC,"timestamp")))

        val meta = PagedMetadata(pageResult.number, pageResult.totalPages, pageResult.numberOfElements)

        return PagedDTO(meta, pageResult.toList().map { it.toDTO() })
    }
}