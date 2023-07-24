package com.lab2.server.dto

import com.lab2.server.data.Attachment
import org.hibernate.type.descriptor.java.BlobJavaType

data class AttachmentDTO (
    val id: Long?,
    val attachment: BlobJavaType,
    val size: Byte,
    val contentType: String,
    val message: Long
)
data class AttachmentBodyDTO (
    val attachment: BlobJavaType,
    val size: Byte,
    val contentType: String,
)

fun Attachment.toDTO(): AttachmentDTO {
    return AttachmentDTO(id, attachment, size, contentType, message?.id!!)
}