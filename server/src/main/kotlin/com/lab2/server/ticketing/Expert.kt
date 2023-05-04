package com.lab2.server.ticketing

import jakarta.persistence.*
import org.aspectj.weaver.IntMap
import javax.management.monitor.StringMonitor

@Entity
@Table (name = "Experts")
class Expert(

    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val expertId: Long,
    var name: String,
    var surname: String
)
{
    @OneToMany(mappedBy = "expert")
    var inProgressTickets = mutableListOf<Ticket>();

    @OneToMany(mappedBy = "expert")
    var changedStatuses = mutableListOf<TicketStatus>();

    fun assignTicket(t: Ticket){
        t.expert = this;
        this.inProgressTickets.add(t);
    }

    fun addTicketStatus(s: TicketStatus){
        s.expert = this;
        this.addTicketStatus(s);
    }
}

