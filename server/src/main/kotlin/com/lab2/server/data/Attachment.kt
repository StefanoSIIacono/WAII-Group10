package com.lab2.server.data

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.type.descriptor.java.BlobJavaType

@Entity
@Table(name = "attachments")
class Attachment(

    val attachment: BlobJavaType,
    val size: Byte,
    val contentType: String,
) : EntityBase<Long>() {
    @ManyToOne(fetch = FetchType.LAZY)
    lateinit var message: Message
}