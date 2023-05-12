package com.lab2.server.dto

import com.lab2.server.data.*

data class TicketDTO(
        val id: Long?,
        val obj: String,
        val arg: String,
        val priority: Priority,
        val profile: ProfileDTO,
        val expert: ExpertDTO?,
        val product: ProductDTO,
        val status: TicketStatusDTO,
)

data class TicketCreateBodyDTO(
        val obj: String,
        val arg: String,
        val profile: ProfileDTO,
        val product: ProductDTO,
)

data class TicketInProgressBodyDTO(
        val expert: ExpertDTO,
        val priority: Priority,
)

fun Ticket.toDTO(): TicketDTO {
            return TicketDTO(id, obj, arg, priority, profile.toDTO(), expert?.toDTO(), product.toDTO(), statusHistory.maxBy { it.timestamp }.toDTO())
}