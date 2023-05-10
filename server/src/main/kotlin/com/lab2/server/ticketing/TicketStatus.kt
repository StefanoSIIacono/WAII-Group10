package com.lab2.server.ticketing

import com.lab2.server.EntityBase
import com.lab2.server.StatusChanger
import com.lab2.server.profiles.Profile
import com.lab2.server.profiles.ProfileDTO
import jakarta.persistence.*

@Entity
@Table(name = "statuses")
class TicketStatus (

    var status: String,

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

fun TicketStatusDTO.toTicketStatus(): TicketStatus {
    return TicketStatus(status, timestamp, statusChanger, ticket, expert?.toExpert())
}

