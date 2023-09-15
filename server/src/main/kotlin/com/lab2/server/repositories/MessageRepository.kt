package com.lab2.server.repositories

import com.lab2.server.data.Message
import com.lab2.server.data.Ticket
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository


@Repository
interface MessageRepository : JpaRepository<Message, Long> {
    fun findFirstByTicketIdOrderByTimestampDesc(ticket_id: Long): Message?
    fun findByTicketIdOrderByTimestampDesc(ticket_id: Long, pageable: Pageable): Page<Message>

    @Query("SELECT t FROM Ticket t WHERE t.profile.email = :email AND t.lastReadMessageIndexProfile < (SELECT MAX(m.index) FROM Message m WHERE m.ticket = t AND m.expert <> NULL)")
    fun findAllProfileUnreadMessages(@Param("email") email: String, pageable: Pageable): Page<Ticket>

    @Query("Select t FROM Ticket t WHERE t.expert.email = :email AND t.lastReadMessageIndexExpert < (SELECT MAX(m.index) FROM Message m WHERE m.ticket = t AND m.expert = NULL)")
    fun findAllExpertUnreadMessages(@Param("email") email: String, pageable: Pageable): Page<Ticket>
}
