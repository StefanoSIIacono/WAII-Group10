package com.lab2.server.controllers

import com.lab2.server.dto.*
import com.lab2.server.exceptionsHandler.exceptions.NoBodyProvidedException
import com.lab2.server.services.MessageService
import io.micrometer.observation.annotation.Observed
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
    fun getTicketMessages(@PathVariable ticketId:Long, principal: Principal): List<MessageDTO>{
        return messageService.getTicketMessages(ticketId, principal.name)
    }
    @PostMapping("/tickets/{ticketId}/messages/paging")
    @ResponseStatus(HttpStatus.OK)
    @Secured("EXPERT", "PROFILE")
    fun getTicketPagedMessages(@PathVariable ticketId:Long, @RequestBody paging: TicketPagingDTO?,principal: Principal): List<MessageDTO>{
        if (paging === null) {
            SqmNode.log.error("Invalid body page messages")
            throw NoBodyProvidedException("You have to add a body")
        }
        return messageService.getTickePagedMessages(ticketId, principal.name, paging)
    }
    @PostMapping("/tickets/{ticketId}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured("EXPERT", "PROFILE")
    fun addMessage(@PathVariable ticketId:Long, @RequestBody message: BodyMessageDTO?, principal: Principal){
        if (message === null) {
            SqmNode.log.error("Invalid body creating message")
            throw NoBodyProvidedException("You have to add a body")
        }
        SqmNode.log.info("Adding new message")
        messageService.handleNewMessage(principal.name, ticketId, message)
    }

    @GetMapping("/tickets/{ticketId}/paging")
    @ResponseStatus(HttpStatus.OK)
    @Secured("EXPERT", "PROFILE")
    fun getTicketPaging(@PathVariable ticketId:Long, principal: Principal): TicketPagingDTO{
        return messageService.getTicketPaging(ticketId, principal.name)
    }
    @PutMapping("/tickets/{ticketId}/paging")
    @ResponseStatus(HttpStatus.OK)
    @Secured("EXPERT", "PROFILE")
    fun ackTicketPaging(@PathVariable ticketId:Long, @RequestBody ack: TicketPagingDTO?,principal: Principal){
        if (ack === null) {
            SqmNode.log.error("Invalid body ack messages")
            throw NoBodyProvidedException("You have to add a body")
        }
        messageService.acknowledgeTicketPaging(ticketId, principal.name,ack)
    }
}