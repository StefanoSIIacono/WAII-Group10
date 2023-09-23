package com.lab2.server.dto

import com.lab2.server.data.Message

data class MessageDTO(
    val id: Long?,
    val index: Int,
    val timestamp: java.util.Date,
    val body: String,
    val attachments: MutableList<AttachmentDTO>,
    val expert: ExpertDTO? = null,
    val ticket: Long
)

data class BodyMessageDTO(
    val body: String,
    val attachments: MutableList<AttachmentBodyDTO>,
)


fun Message.toDTO(): MessageDTO {
    return MessageDTO(
        id,
        index,
        timestamp,
        body,
        attachments.map { it.toDTO() }.toMutableList(),
        expert?.toDTO(),
        ticket.id!!
    )
}