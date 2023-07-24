package com.lab2.server.data

import jakarta.persistence.*

@Entity
@Table(name="messages")
class Message (

    @Temporal(TemporalType.TIMESTAMP)
    val timestamp: java.util.Date,
    val body: String,

    // This field is filled only whether the message was written by the expert
    @ManyToOne
    val expert: Expert? = null,

    @ManyToOne
    var ticket: Ticket? = null

): EntityBase<Long>() {


    @OneToMany(mappedBy = "message")
    var attachments = mutableListOf<Attachment>()

    fun addAttachment(a: Attachment){
        a.message = this
        this.attachments.add(a)
    }
}

/*
fun MessageDTO.toMessage(): Message{
    val message = Message(timestamp, body, expert?.toExpert(), ticket)
    message.id = id
    return message

}
 */
