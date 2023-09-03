package com.lab2.server.services;

import com.lab2.server.dto.BodyMessageDTO
import com.lab2.server.dto.MessageDTO
import com.lab2.server.dto.TicketPagingDTO

interface MessageService {
    fun handleNewMessage(sender: String, ticketId: Long, messageDTO: BodyMessageDTO)
    fun getTicketMessages(ticketID: Long, user: String): List<MessageDTO>
    fun getTicketPaging(ticketID: Long, user: String): TicketPagingDTO
    fun acknowledgeTicketPaging(ticketID: Long, user: String, ack:TicketPagingDTO)
    fun getTickePagedMessages(ticketID: Long, user: String, paging:TicketPagingDTO): List<MessageDTO>
}