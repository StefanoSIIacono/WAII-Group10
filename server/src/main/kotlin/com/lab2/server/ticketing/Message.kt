package com.lab2.server.ticketing

import com.lab2.server.Chatter
import com.lab2.server.EntityBase
import jakarta.persistence.*

@Entity
class Message (

    @Temporal(TemporalType.TIMESTAMP)
    val timestamp: java.util.Date,
    val body: String,

    // TODO: la superclasse chatter deve essere ancora creata, momentaneamente è una stringa
    val chatter: String,

    @ManyToOne
    var ticket: Ticket
): EntityBase<Long>() {
    @OneToMany(mappedBy = "message")
    var attachments = mutableListOf<Attachment>();

    fun addAttachment(a: Attachment){
        a.message = this;
        this.attachments.add(a);
    }
}