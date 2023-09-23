package com.lab2.server.data

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "attachments")
class Attachment(
    val attachment: String,
    val size: Long,
    val contentType: String,
) : EntityBase<Long>() {
    @ManyToOne(fetch = FetchType.LAZY)
    lateinit var message: Message
}