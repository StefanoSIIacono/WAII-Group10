package com.lab2.server.repositories

import com.lab2.server.data.Expert
import com.lab2.server.dto.Chart1Data
import com.lab2.server.dto.Chart2Data
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ExpertRepository : JpaRepository<Expert, String> {
    fun findByEmailContaining(email: String, pageable: Pageable): Page<Expert>
    fun findByEmailContainingAndExpertisesField(
        email: String,
        expertisesField: String,
        pageable: Pageable
    ): Page<Expert>

    @Query("Select COUNT(t) FROM Ticket t WHERE t.expert.email = :email")
    fun countCurrentlyAssignedToExpert(@Param("email") email: String): Long?

    @Query("Select COUNT(s) FROM TicketStatus s WHERE s.expert.email = :email")
    fun countAllEverAssignedToExpert(@Param("email") email: String): Long?

    @Query("SELECT COUNT(s) FROM TicketStatus s, TicketStatus c WHERE c.expert.email = :email AND c.ticket = s.ticket AND c.timestamp < s.timestamp AND s.status != 1 AND (SELECT MAX(x.timestamp) FROM TicketStatus x WHERE x.timestamp > c.timestamp AND x.timestamp < s.timestamp AND x.ticket = c.ticket) IS NULL")
    fun totalClosed(@Param("email") email: String): Long?

    @Query(
        "SELECT SUM(EXTRACT(EPOCH FROM s.timestamp - c.timestamp)) FROM statuses s, statuses c WHERE c.expert_email = :email AND c.ticket_id = s.ticket_id AND c.timestamp < s.timestamp AND s.status != 1 AND (SELECT MAX(x.timestamp) FROM statuses x WHERE x.timestamp > c.timestamp AND x.timestamp < s.timestamp AND x.ticket_id = c.ticket_id) IS NULL",
        nativeQuery = true
    )
    fun totalTimeToSolveTickets(@Param("email") email: String): Long?

    @Query("SELECT new com.lab2.server.dto.Chart1Data(to_char(s.timestamp,'YYYY/MM/DD'), COUNT(s)) FROM TicketStatus s WHERE s.status != 1 AND (SELECT MAX(c.timestamp) FROM TicketStatus c WHERE  c.expert.email = :email AND c.ticket = s.ticket AND c.timestamp < s.timestamp AND (SELECT MAX(x.timestamp) FROM TicketStatus x WHERE x.timestamp > c.timestamp AND x.timestamp < s.timestamp AND x.ticket = c.ticket) IS NULL) IS NOT NULL GROUP BY to_char(s.timestamp,'YYYY/MM/DD')")
    fun closedPerDays(@Param("email") email: String): List<Chart1Data>

    @Query("SELECT new com.lab2.server.dto.Chart2Data(t.arg.field, COUNT(s)) FROM TicketStatus s, Ticket  t WHERE t = s.ticket AND s.status != 1 AND  (SELECT MAX(c.timestamp) FROM TicketStatus c WHERE  c.expert.email = :email AND c.ticket = s.ticket AND c.timestamp < s.timestamp AND (SELECT MAX(x.timestamp) FROM TicketStatus x WHERE x.timestamp > c.timestamp AND x.timestamp < s.timestamp AND x.ticket = t) IS NULL) IS NOT NULL GROUP BY t.arg.field")
    fun closedPerExpertise(@Param("email") email: String): List<Chart2Data>
}