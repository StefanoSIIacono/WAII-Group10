package com.lab2.server.data

import jakarta.persistence.*

@Entity
@Table(name = "attachments")
class Attachment(
    @Column(length = 7000000)
    val attachment: String,
    val size: Long,
    val contentType: String,
) : EntityBase<Long>() {
    @ManyToOne(fetch = FetchType.LAZY)
    lateinit var message: Message
}