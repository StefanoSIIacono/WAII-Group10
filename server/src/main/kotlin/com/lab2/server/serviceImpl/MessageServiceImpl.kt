package com.lab2.server.serviceImpl

import com.lab2.server.data.Message
import com.lab2.server.dto.BodyMessageDTO
import com.lab2.server.dto.MessageDTO
import com.lab2.server.dto.toDTO
import com.lab2.server.exceptionsHandler.exceptions.TicketNotFoundException
import com.lab2.server.repositories.MessageRepository
import com.lab2.server.repositories.TicketingRepository
import com.lab2.server.services.MessageService
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

        val message = Message(Date(System.currentTimeMillis()), messageDTO.body, if (ticket.expert?.email == sender) ticket.expert else null)

        ticket.addMessage(message)
        messageRepository.save(message)
        ticketingRepository.save(ticket)
    }
    override fun getTicketMessages(ticketID: Long, user: String): List<MessageDTO> {
        val ticket = ticketingRepository.findByIdOrNull(ticketID)
            ?: throw TicketNotFoundException("Ticket not found")

        if (ticket.expert?.email != user && ticket.profile.email != user) {
            throw TicketNotFoundException("No ticket found")
        }

        return ticket.messages.map { it.toDTO() }
    }
}