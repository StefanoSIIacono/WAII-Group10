package com.lab2.server.dto

import com.lab2.server.data.Message
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType

data class MessageDTO (
    @Temporal(TemporalType.TIMESTAMP)
    val timestamp: java.util.Date,
    val body: String,
    val chatter: String,
    val attachments: MutableList<AttachmentDTO> = mutableListOf(),
)

fun Message.toDTO(): MessageDTO {
    return MessageDTO(timestamp, body, chatter, attachments.map {it.toDTO()}.toMutableList())
}