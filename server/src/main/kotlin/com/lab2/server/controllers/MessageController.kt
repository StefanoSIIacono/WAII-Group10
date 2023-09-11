package com.lab2.server.controllers

import com.lab2.server.dto.*
import com.lab2.server.services.MessageService
import io.micrometer.observation.annotation.Observed
import jakarta.transaction.Transactional
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import lombok.extern.slf4j.Slf4j
import org.hibernate.query.sqm.tree.SqmNode
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@Slf4j
@Observed
class MessageController(private val messageService: MessageService) {

    @GetMapping("/tickets/{ticketId}/messages")
    @ResponseStatus(HttpStatus.OK)
    @Secured("MANAGER", "EXPERT", "PROFILE")
    fun getPagedTicketMessages(
        @PathVariable ticketId: Long,
        @RequestParam(required = false, defaultValue = "1") @Min(1) page: Int,
        @RequestParam(required = false, defaultValue = "100") @Min(1) @Max(100) offset: Int,
        principal: JwtAuthenticationToken
    ): PagedDTO<MessageDTO> {
        return messageService.getTicketPagedMessages(ticketId, principal, page - 1, offset)
    }

    @PostMapping("/tickets/{ticketId}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured("EXPERT", "PROFILE")
    @Transactional
    fun addMessage(
        @PathVariable ticketId: Long,
        @RequestBody(required = true) message: BodyMessageDTO,
        principal: JwtAuthenticationToken
    ) {
        SqmNode.log.info("Adding new message")
        messageService.handleNewMessage(principal, ticketId, message)
    }

    @PutMapping("/tickets/{ticketId}/messages/{messageIndex}/ack")
    @ResponseStatus(HttpStatus.OK)
    @Secured("EXPERT", "PROFILE")
    @Transactional
    fun ackTicketPaging(
        @PathVariable ticketId: Long,
        @PathVariable messageIndex: Int,
        principal: JwtAuthenticationToken
    ) {
        messageService.acknowledgeMessage(ticketId, principal, messageIndex)
    }
}