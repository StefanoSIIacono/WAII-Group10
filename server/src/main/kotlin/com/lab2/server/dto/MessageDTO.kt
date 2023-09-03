package com.lab2.server.dto

import com.lab2.server.data.Message
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType

data class MessageDTO (
    val id: Long?,
    @Temporal(TemporalType.TIMESTAMP)
    val timestamp: java.util.Date,
    val body: String,
    val attachments: MutableList<AttachmentDTO>,
    val expert: ExpertDTO? = null,
    val ticket: Long
)

data class BodyMessageDTO (
    val body: String,
    val attachments: MutableList<AttachmentBodyDTO>,
)

data class RawMessage (
    val message: String
)


fun Message.toDTO(): MessageDTO {
    return MessageDTO(id, timestamp, body, attachments.map {it.toDTO()}.toMutableList(), expert?.toDTO(), ticket?.id!!)
}

fun RawMessage.toBodyMessageDTO(): BodyMessageDTO {
    return BodyMessageDTO(message, mutableListOf())
}