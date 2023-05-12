package com.lab2.server.dto

import com.lab2.server.data.Priority
import com.lab2.server.data.Ticket

data class TicketDTO(
        val id: Long?,
        val obj: String,
        val arg: String,
        val priority: Priority,
        val profile: String,
        val expert: Long?,
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
        val priority: Priority,
)

fun Ticket.toDTO(): TicketDTO {
            return TicketDTO(id, obj, arg, priority, profile.email, expert?.id, product.id, statusHistory.maxBy { it.timestamp }.toDTO())
}