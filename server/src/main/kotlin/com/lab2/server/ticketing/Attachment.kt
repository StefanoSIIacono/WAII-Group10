package com.lab2.server.ticketing

import jakarta.persistence.*
import org.hibernate.type.descriptor.java.BlobJavaType
import java.sql.Blob

@Entity
@Table(name="attachments")
class Attachment(
    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val attachmentId: Long,

    val attachment: BlobJavaType,
    val size: Byte,
    val contentType: String,

    @ManyToOne
    @JoinColumn(name="message_id", nullable = false)
    var message: Message,
)
{

}