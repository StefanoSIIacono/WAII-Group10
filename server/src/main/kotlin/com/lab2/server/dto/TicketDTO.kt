package com.lab2.server.dto

import com.lab2.server.data.Priority
import com.lab2.server.data.Ticket

data class TicketDTO(
        val id: Long?,
        val obj: String,
        val arg: ExpertiseDTO,
        val priority: Priority,
        val profile: String,
        val expert: String?,
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
        val expert: String,
        val priority: Priority,
)

fun Ticket.toDTO(): TicketDTO {
            return TicketDTO(
                    this.id,
                    this.obj,
                    this.arg.toDTO(),
                    this.priority,
                    this.profile.email,
                    this.expert?.email,
                    this.product.id,
                    this.statusHistory.maxBy { it.timestamp }.toDTO()
            )
}