package com.lab2.server.data

import com.lab2.server.dto.AttachmentDTO
import jakarta.persistence.*
import org.hibernate.type.descriptor.java.BlobJavaType

@Entity
@Table(name="attachments")
class Attachment(

    val attachment: BlobJavaType,
    val size: Byte,
    val contentType: String,

    @ManyToOne
    var message: Message,
): EntityBase<Long>()

fun AttachmentDTO.toAttachment(): Attachment {
    val att = Attachment(attachment, size, contentType, message.toMessage())
    att. id = id
    return att
}
