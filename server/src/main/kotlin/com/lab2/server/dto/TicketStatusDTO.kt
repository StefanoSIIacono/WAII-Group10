package com.lab2.server.dto

import com.lab2.server.data.Roles
import com.lab2.server.data.Status
import com.lab2.server.data.TicketStatus

data class TicketStatusDTO(
    val status: Status,
    val timestamp: java.util.Date,
    val statusChanger: Roles,
    val expert: String?,
)

fun TicketStatus.toDTO(): TicketStatusDTO {
    return TicketStatusDTO(
        this.status,
        this.timestamp,
        this.statusChanger,
        this.expert?.email
    )
}