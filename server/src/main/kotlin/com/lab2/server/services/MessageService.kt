package com.lab2.server.services;

import com.lab2.server.dto.BodyMessageDTO
import com.lab2.server.dto.MessageDTO

interface MessageService {
    fun handleNewMessage(sender: String, ticketId: Long, messageDTO: BodyMessageDTO)
    fun getTicketMessages(ticketID: Long, user: String): List<MessageDTO>
}