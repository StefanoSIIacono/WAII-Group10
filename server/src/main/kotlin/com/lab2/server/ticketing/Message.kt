package com.lab2.server.ticketing

import jakarta.persistence.*

@Entity
class Message (

    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val messageId: Long,

    @Temporal(TemporalType.TIMESTAMP)
    val timestamp: java.util.Date,
    val body: String,

    // TODO: la superclasse chatter deve essere ancora creata, momentaneamente è una stringa
    val chatter: String,

    @ManyToOne
    @JoinColumn(name="ticket_id", nullable = false)
    var ticket: Ticket
){
    @OneToMany(mappedBy = "message")
    var attachments = mutableListOf<Attachment>();

    fun addAttachment(a: Attachment){
        a.message = this;
        this.attachments.add(a);
    }
}