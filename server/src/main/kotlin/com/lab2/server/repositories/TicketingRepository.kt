package com.lab2.server.repositories

import com.lab2.server.data.Ticket
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketingRepository : JpaRepository<Ticket, Long> {
    fun findAllByProfileEmail(email: String, pageable: Pageable): Page<Ticket>
    fun findAllByProfileEmail(email: String): List<Ticket>
    fun findAllByExpertEmail(email: String): List<Ticket>
}