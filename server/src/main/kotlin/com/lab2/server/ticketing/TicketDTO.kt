package com.lab2.server.ticketing

import com.lab2.server.Chatter
import com.lab2.server.StatusChanger
import com.lab2.server.products.ProductDTO
import com.lab2.server.products.toDTO
import com.lab2.server.profiles.ProfileDTO
import com.lab2.server.profiles.toDTO
import org.hibernate.type.descriptor.java.BlobJavaType

data class ExpertDTO(
        val name: String,
        val surname: String,
)

data class TicketStatusDTO (
        val status: String,
        val timestamp: java.util.Date,
        val statusChanger: String,
        val expert: ExpertDTO?,
)

data class MessageDTO (
        val timestamp: java.util.Date,
        val body: String,
        val chatter: String,
        val attachments: MutableList<AttachmentDTO> = mutableListOf(),
)

data class AttachmentDTO (
        val attachment: BlobJavaType,
        val size: Byte,
        val contentType: String,
)
data class TicketDTO(

        val obj: String,
        val arg: String,
        val priority: Int,
        val profile: ProfileDTO,

        val expert: ExpertDTO?,
        val product: ProductDTO,
        val statusHistory: MutableList<TicketStatusDTO> = mutableListOf(),
        val messages: MutableList<MessageDTO> = mutableListOf()
)


fun Expert.toDTO(): ExpertDTO {
    return ExpertDTO(name, surname)
}

fun Message.toDTO(): MessageDTO {
    return MessageDTO(timestamp, body, chatter, attachments.map {it.toDTO()}.toMutableList())
}

fun TicketStatus.toDTO(): TicketStatusDTO {
    return TicketStatusDTO(status, timestamp, statusChanger, expert?.toDTO())
}

fun Attachment.toDTO(): AttachmentDTO {
    return AttachmentDTO(attachment, size, contentType)
}

fun Ticket.toDTO(): TicketDTO {
    return TicketDTO(obj, arg, priority, profile.toDTO(), expert?.toDTO(), product.toDTO(), statusHistory.map{ it.toDTO() }.toMutableList(), messages.map { it.toDTO() }.toMutableList())
}