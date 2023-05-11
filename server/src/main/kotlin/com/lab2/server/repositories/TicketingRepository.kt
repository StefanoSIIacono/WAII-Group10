package com.lab2.server.repositories

import com.lab2.server.data.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketingRepository: JpaRepository<Ticket, Long> {
}