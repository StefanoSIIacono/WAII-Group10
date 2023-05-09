package com.lab2.server.ticketing

import com.lab2.server.EntityBase
import com.lab2.server.StatusChanger
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