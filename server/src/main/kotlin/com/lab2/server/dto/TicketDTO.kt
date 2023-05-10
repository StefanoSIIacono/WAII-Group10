package com.lab2.server.dto

import com.lab2.server.data.*

data class TicketDTO(
        val id: Long?,
        val obj: String,
        val arg: String,
        val priority: Priority,
        val profile: String,
        val expert: ExpertDTO?,
        val product: String,
        val status: TicketStatusDTO,
)

data class TicketCreateBodyDTO(
        val obj: String,
        val arg: String,
        val profile: String,
        val product: String,
)

data class TicketInProgressBodyDTO(
        val expert: Long,
)


fun Ticket.toDTO(): TicketDTO {
            return TicketDTO(this.getId(), obj, arg, priority, profile.email, expert?.toDTO(), product.id, statusHistory.maxBy { it.timestamp }.toDTO())
}