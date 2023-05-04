package com.lab2.server.ticketing

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketingRepository: JpaRepository<Ticket, String> {
}