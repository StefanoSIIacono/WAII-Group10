package com.lab2.server.dto

import com.lab2.server.data.Attachment
import org.springframework.web.multipart.MultipartFile

data class AttachmentDTO(
    val id: Long?,
    val attachment: ByteArray,
    val size: Long,
    val contentType: String,
    val message: Long
)

data class AttachmentBodyDTO(
    val attachment: MultipartFile,
    val contentType: String,
)

fun Attachment.toDTO(): AttachmentDTO {
    return AttachmentDTO(id, attachment, size, contentType, message.id!!)
}