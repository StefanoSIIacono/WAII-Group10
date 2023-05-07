package com.lab2.server.ticketing

import com.lab2.server.EntityBase
import jakarta.persistence.*
import org.aspectj.weaver.IntMap
import javax.management.monitor.StringMonitor

@Entity
@Table (name = "experts")
class Expert(
    var name: String,
    var surname: String
): EntityBase<Long>()
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

