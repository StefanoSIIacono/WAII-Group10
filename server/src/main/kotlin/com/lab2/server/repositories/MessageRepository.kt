package com.lab2.server.repositories

import com.lab2.server.data.Message
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface MessageRepository : JpaRepository<Message, Long> {
    fun findFirstByTicketIdOrderByTimestampDesc(ticket_id: Long): Message?
    fun findByTicketIdOrderByTimestampDesc(ticket_id: Long, pageable: Pageable): Page<Message>
}