package com.lab2.server.dto

import com.lab2.server.data.Attachment

data class AttachmentDTO(
    val id: Long?,
    val attachment: String,
    val size: Long,
    val contentType: String,
    val message: Long
)

data class AttachmentBodyDTO(
    val attachment: String,
    val contentType: String,
)

fun Attachment.toDTO(): AttachmentDTO {
    return AttachmentDTO(id, attachment, size, contentType, message.id!!)
}