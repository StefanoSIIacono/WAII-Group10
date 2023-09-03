package com.lab2.server.serviceImpl

import com.lab2.server.data.Message
import com.lab2.server.dto.BodyMessageDTO
import com.lab2.server.dto.MessageDTO
import com.lab2.server.dto.TicketPagingDTO
import com.lab2.server.dto.toDTO
import com.lab2.server.exceptionsHandler.exceptions.TicketNotFoundException
import com.lab2.server.repositories.MessageRepository
import com.lab2.server.repositories.TicketingRepository
import com.lab2.server.services.MessageService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*
val PAGESIZE=10
@Service
class MessageServiceImpl(private val ticketingRepository: TicketingRepository, private val messageRepository: MessageRepository): MessageService {
    override fun handleNewMessage(sender: String, ticketId: Long, messageDTO: BodyMessageDTO) {
        val ticket = ticketingRepository.findByIdOrNull(ticketId)
            ?: throw TicketNotFoundException("Ticket not found")

        if (ticket.expert?.email != sender && ticket.profile.email != sender) {
            throw TicketNotFoundException("No ticket found")
        }

        val message = Message(Date(System.currentTimeMillis()), messageDTO.body, if (ticket.expert?.email == sender) ticket.expert else null)
        if (ticket.expert?.email == sender){
            ticket.indexE+=1
            ticket.offsetE+=1
            ticket.offsetP+=1
        }
        else{
            ticket.indexP+=1
            ticket.offsetP+=1
            ticket.offsetE+=1
        }

        ticket.addMessage(message)
        //useful or not(?)
        //ticket.messages.sortBy { message: Message -> message.timestamp  }
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

    override fun getTicketPaging(ticketID: Long, user: String): TicketPagingDTO {
        val ticket = ticketingRepository.findByIdOrNull(ticketID)
            ?: throw TicketNotFoundException("Ticket not found")

        if (ticket.expert?.email != user && ticket.profile.email != user) {
            throw TicketNotFoundException("No ticket found")
        }
        if (ticket.expert?.email == user){
            return TicketPagingDTO(index = ticket.indexE, offset = ticket.offsetE)
        }
        else{
            return TicketPagingDTO(index = ticket.indexP, offset = ticket.offsetP)
        }
    }

    override fun acknowledgeTicketPaging(ticketID: Long, user: String, ack: TicketPagingDTO) {
        val ticket = ticketingRepository.findByIdOrNull(ticketID)
            ?: throw TicketNotFoundException("Ticket not found")

        if (ticket.expert?.email != user && ticket.profile.email != user) {
            throw TicketNotFoundException("No ticket found")
        }
        if (ticket.expert?.email == user){
            ticket.indexE=ack.offset
        }
        else{
            ticket.indexP=ack.offset
        }
    }

    override fun getTickePagedMessages(ticketID: Long, user: String, paging: TicketPagingDTO):List<MessageDTO> {
        val ticket = ticketingRepository.findByIdOrNull(ticketID)
            ?: throw TicketNotFoundException("Ticket not found")

        if (ticket.expert?.email != user && ticket.profile.email != user) {
            throw TicketNotFoundException("No ticket found")
        }
        var result= listOf<MessageDTO>()
        var page: Int = paging.index/ PAGESIZE
        var cutAtFirst= PAGESIZE-(paging.index%10)
        var cutAtLast=paging.offset%PAGESIZE
        var done=false
        while(page<=paging.offset/ PAGESIZE) {

            var tmp =
                messageRepository.findAll(PageRequest.of(page, PAGESIZE, Sort.by("timestamp"))).toList().map { it.toDTO() }
            if (!done){
                tmp=tmp.takeLast(cutAtFirst)
                done=true
            }
            if(page==paging.offset/ PAGESIZE){
                tmp=tmp.take(cutAtLast)
            }

            result=result+tmp
            page+=1
        }
        return result
    }
}