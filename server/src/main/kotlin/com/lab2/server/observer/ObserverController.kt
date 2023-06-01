package com.lab2.server.observer

import com.lab2.server.dto.TicketCreateBodyDTO
import com.lab2.server.exceptionsHandler.exceptions.NoBodyProvidedException
import com.lab2.server.repositories.TicketingRepository
import com.lab2.server.serviceImpl.TicketServiceImpl
import com.lab2.server.services.TicketService
import io.micrometer.observation.annotation.Observed
import jakarta.transaction.Transactional
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
//@RequestMapping( value = "api/v2/ticketing")
@Slf4j
@Observed
class ObserverController(){//private val ticketService: TicketService) {
    // maybe logger is into the service
    companion object{
        private val log = LoggerFactory.getLogger(ObserverController::class.java)
    }
}