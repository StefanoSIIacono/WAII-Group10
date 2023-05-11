package com.lab2.server.dto

import com.lab2.server.data.Status
import com.lab2.server.data.StatusChanger
import com.lab2.server.data.TicketStatus
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType

data class TicketStatusDTO (
    val status: Status,

    @Temporal(TemporalType.TIMESTAMP)
    val timestamp: java.util.Date,


    val ticket: TicketDTO?,

    val statusChanger: StatusChanger,

    val expert: ExpertDTO?,
)

fun TicketStatus.toDTO(): TicketStatusDTO {
    return TicketStatusDTO(status, timestamp, ticket?.toDTO(), statusChanger, expert?.toDTO())
}