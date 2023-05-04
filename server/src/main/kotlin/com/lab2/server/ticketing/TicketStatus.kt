package com.lab2.server.ticketing

import jakarta.persistence.*

@Entity
@Table(name = "Statuses")
class TicketStatus (

    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    val statusId: Long,
    var status: String,

    @Temporal(TemporalType.TIMESTAMP)
    val timestamp: java.util.Date,

    // TODO: da implementare le superclassi che possono cambiare lo status, momentaneamente stringa
    val author: String,

    @ManyToOne
    @JoinColumn(name="ticket_id", nullable = false)
    var ticket: Ticket,

    @ManyToOne
    @JoinColumn(name="expert_id", nullable = true)
    var expert: Expert? = null,
)
{

}