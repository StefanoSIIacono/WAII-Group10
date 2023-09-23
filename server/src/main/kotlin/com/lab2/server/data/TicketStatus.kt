package com.lab2.server.data

import jakarta.persistence.*


@Entity
@Table(name = "statuses")
class TicketStatus(

    var status: Status,

    @Temporal(TemporalType.TIMESTAMP)
    val timestamp: java.util.Date,

    ) : EntityBase<Long>() {
    @ManyToOne
    lateinit var ticket: Ticket

    @Enumerated(value = EnumType.STRING)
    lateinit var statusChanger: Roles

    @ManyToOne
    var expert: Expert? = null
}
