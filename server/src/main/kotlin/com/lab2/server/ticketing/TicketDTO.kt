package com.lab2.server.ticketing

import com.lab2.server.products.ProductDTO
import com.lab2.server.products.toDTO
import com.lab2.server.profiles.ProfileDTO
import com.lab2.server.profiles.toDTO
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import org.hibernate.type.descriptor.java.BlobJavaType
import org.jetbrains.annotations.NotNull

data class ExpertDTO(
        val name: String,
        val surname: String,
        val expertises: MutableSet<ExpertiseDTO> = mutableSetOf()
)

data class TicketStatusDTO (
        val status: Status,
        @Temporal(TemporalType.TIMESTAMP)
        val timestamp: java.util.Date,
        val statusChanger: String,
        val expert: ExpertDTO?,
)

data class MessageDTO (
        @Temporal(TemporalType.TIMESTAMP)
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

fun Expert.toDTO(): ExpertDTO {
    return ExpertDTO(name, surname, expertises.map { it.toDTO()}.toMutableSet())
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
            return TicketDTO(this.getId(), obj, arg, priority, profile.email, expert?.toDTO(), product.id, statusHistory.maxBy { it.timestamp }.toDTO())
}