package com.lab2.server.data

import jakarta.persistence.*

@Entity
@Table(name = "statuses")
class TicketStatus (

    var status: Status,

    @Temporal(TemporalType.TIMESTAMP)
    val timestamp: java.util.Date,

    // TODO: da implementare le superclassi che possono cambiare lo status, momentaneamente stringa
    val statusChanger: String,

    @ManyToOne
    var ticket: Ticket,

    @ManyToOne
    var expert: Expert? = null,
): EntityBase<Long>()
{

}

/*
fun TicketStatusDTO.toTicketStatus(): TicketStatus {
    return TicketStatus(status, timestamp, statusChanger, ticket, expert?.toExpert())
}
*/
