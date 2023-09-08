package com.lab2.server.controllers

import com.lab2.server.dto.*
import com.lab2.server.exceptionsHandler.exceptions.NoBodyProvidedException
import com.lab2.server.services.MessageService
import io.micrometer.observation.annotation.Observed
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import lombok.extern.slf4j.Slf4j
import org.hibernate.query.sqm.tree.SqmNode
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@Slf4j
@Observed
class MessageController(private val messageService: MessageService) {

    @GetMapping("/tickets/{ticketId}/messages")
    @ResponseStatus(HttpStatus.OK)
    @Secured("MANAGER", "EXPERT", "PROFILE")
    fun getPagedTicketMessages(
        @PathVariable ticketId:Long,
        @RequestParam(required = false, defaultValue = "1") @Min(1) page: Int,
        @RequestParam(required = false, defaultValue = "100") @Min(1) @Max(100) offset: Int,
        principal: Principal
    ): PagedDTO<MessageDTO>{
        return messageService.getTicketPagedMessages(ticketId, principal.name, page, offset)
    }

    @PostMapping("/tickets/{ticketId}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured("EXPERT", "PROFILE")
    fun addMessage(@PathVariable ticketId:Long, @RequestBody(required = true) message: BodyMessageDTO, principal: Principal){
        SqmNode.log.info("Adding new message")
        messageService.handleNewMessage(principal.name, ticketId, message)
    }

    @PutMapping("/tickets/{ticketId}/messages/ack")
    @ResponseStatus(HttpStatus.OK)
    @Secured("EXPERT", "PROFILE")
    fun ackTicketPaging(@PathVariable ticketId: Long, @RequestBody(required = true) ack: MessageReadAck, principal: Principal){
        messageService.acknowledgeMessage(ticketId, principal.name, ack)
    }
}