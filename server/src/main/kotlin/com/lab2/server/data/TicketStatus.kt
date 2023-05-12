package com.lab2.server.data

import jakarta.persistence.*


@Entity
@Table(name = "statuses")
class TicketStatus (

    var status: Status,

    @Temporal(TemporalType.TIMESTAMP)
    val timestamp: java.util.Date,

    @ManyToOne
    var ticket: Ticket,

    @Enumerated(value = EnumType.STRING)
    val statusChanger: StatusChanger = StatusChanger.PROFILE,

    @ManyToOne
    var expert: Expert? = null,

): EntityBase<Long>()

/*
fun TicketStatusDTO.toTicketStatus(): TicketStatus {
    return TicketStatus(status, timestamp, ticket, statusChanger, expert?.toExpert())
}
*/
