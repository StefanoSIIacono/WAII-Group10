package com.lab2.server.repositories

import com.lab2.server.data.TicketStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StatusRepository : JpaRepository<TicketStatus, Long>