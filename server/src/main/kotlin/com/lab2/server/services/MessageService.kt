package com.lab2.server.services

import com.lab2.server.dto.BodyMessageDTO
import com.lab2.server.dto.MessageDTO
import com.lab2.server.dto.PagedDTO
import com.lab2.server.dto.UnreadMessagesDTO
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

interface MessageService {
    fun handleNewMessage(sender: JwtAuthenticationToken, ticketId: Long, messageDTO: BodyMessageDTO)
    fun acknowledgeMessage(ticketID: Long, user: JwtAuthenticationToken, ack: Int?)
    fun getTicketPagedMessages(
        ticketID: Long,
        user: JwtAuthenticationToken,
        page: Int,
        offset: Int
    ): PagedDTO<MessageDTO>

    fun getUnreadMessages(
        user: JwtAuthenticationToken,
        page: Int,
        offset: Int
    ): PagedDTO<UnreadMessagesDTO>
}