package com.lab2.server.data

import jakarta.persistence.*

@Entity
@Table(name = "messages")
class Message(

    @Temporal(TemporalType.TIMESTAMP)
    val timestamp: java.util.Date,
    val body: String,

    @OneToMany(mappedBy = "message", cascade = [CascadeType.ALL])
    var attachments: MutableList<Attachment> = mutableListOf()

) : EntityBase<Long>() {

    // This field is filled only whether the message was written by the expert
    @ManyToOne
    var expert: Expert? = null

    @ManyToOne
    lateinit var ticket: Ticket

    fun addAttachments(a: List<Attachment>) {
        a.forEach {
            it.message = this
            this.attachments.add(it)
        }
    }
}
