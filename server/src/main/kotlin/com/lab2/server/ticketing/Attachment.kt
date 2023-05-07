package com.lab2.server.ticketing

import com.lab2.server.EntityBase
import jakarta.persistence.*
import org.hibernate.type.descriptor.java.BlobJavaType
import java.sql.Blob

@Entity
@Table(name="attachments")
class Attachment(

    val attachment: BlobJavaType,
    val size: Byte,
    val contentType: String,

    @ManyToOne
    var message: Message,
): EntityBase<Long>()
{

}