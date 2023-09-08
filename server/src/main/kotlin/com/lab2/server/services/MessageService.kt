package com.lab2.server.services;

import com.lab2.server.dto.*

interface MessageService {
    fun handleNewMessage(sender: String, ticketId: Long, messageDTO: BodyMessageDTO)
    fun acknowledgeMessage(ticketID: Long, user: String, ack: MessageReadAck)
    fun getTicketPagedMessages(ticketID: Long, user: String, page: Int, offset: Int): PagedDTO<MessageDTO>
}