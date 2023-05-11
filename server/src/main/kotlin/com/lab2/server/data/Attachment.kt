package com.lab2.server.data

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
