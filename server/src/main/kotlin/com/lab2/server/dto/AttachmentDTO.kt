package com.lab2.server.dto

import com.lab2.server.data.Attachment
import org.hibernate.type.descriptor.java.BlobJavaType

data class AttachmentDTO (
    val attachment: BlobJavaType,
    val size: Byte,
    val contentType: String,
)

fun Attachment.toDTO(): AttachmentDTO {
    return AttachmentDTO(attachment, size, contentType)
}