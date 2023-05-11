package com.lab2.server.data

import com.lab2.server.dto.ExpertDTO
import jakarta.persistence.*

// ------------------------------- EXPERT -------------------------------
@Entity
@Table (name = "experts")
class Expert(
    var name: String,
    var surname: String,

    ): EntityBase<Long>()
{
    @OneToMany(mappedBy = "expert")
    var inProgressTickets = mutableListOf<Ticket>()

    @OneToMany(mappedBy = "expert")
    var changedStatuses = mutableListOf<TicketStatus>()

    @ManyToMany
    @JoinTable(name = "expert_expertise",
        joinColumns = [JoinColumn(name="expert_id")],
        inverseJoinColumns = [JoinColumn(name = "expertise_id")]
    )
    val expertises: MutableSet<Expertise> = mutableSetOf()

    fun assignTicket(t: Ticket){
        t.expert = this
        this.inProgressTickets.add(t)
    }

    fun addTicketStatus(s: TicketStatus){
        s.expert = this
        this.addTicketStatus(s)
    }

    fun addExpertise(e: Expertise) {
        expertises.add(e)
        e.experts.add(this)
    }

}

fun ExpertDTO.toExpert(): Expert {
    return Expert(name, surname)
}
