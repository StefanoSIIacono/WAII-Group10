package com.lab2.server.dto

import com.lab2.server.data.Message
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType

data class MessageDTO (
    val id: Long?,
    @Temporal(TemporalType.TIMESTAMP)
    val timestamp: java.util.Date,
    val body: String,
    val attachments: MutableList<AttachmentDTO> = mutableListOf(),
    val expert: ExpertDTO? = null,
    val ticket: Long
)

fun Message.toDTO(): MessageDTO {
    return MessageDTO(id, timestamp, body, attachments.map {it.toDTO()}.toMutableList(), expert?.toDTO(), ticket.id!!)
}