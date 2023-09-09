package com.lab2.server.data

import jakarta.persistence.*

@Entity
@Table(name = "attachments")
class Attachment(

    @Lob
    val attachment: ByteArray,
    val size: Long,
    val contentType: String,
) : EntityBase<Long>() {
    @ManyToOne(fetch = FetchType.LAZY)
    lateinit var message: Message
}