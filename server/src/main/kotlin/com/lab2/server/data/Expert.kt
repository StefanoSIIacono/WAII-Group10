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
    @ManyToMany
    @JoinTable(name = "expert_expertise",
            joinColumns = [JoinColumn(name="expert_id")],
            inverseJoinColumns = [JoinColumn(name = "expertise_id")]
    )
    val expertises: MutableSet<Expertise> = mutableSetOf()

    @OneToMany(mappedBy = "expert")
    var inProgressTickets = mutableListOf<Ticket>()

    @OneToMany(mappedBy = "expert")
    var changedStatuses = mutableListOf<TicketStatus>()

    @OneToMany( mappedBy = "expert")
    var messages: MutableSet<Message> = mutableSetOf()

    fun assignTicket(t: Ticket){
        t.expert = this
        this.inProgressTickets.add(t)
    }


    fun addTicketStatus(s: TicketStatus){
        s.expert = this
        this.changedStatuses.add(s)
    }

    fun addExpertise(e: Expertise) {
        expertises.add(e)
        e.experts.add(this)
    }

}

fun ExpertDTO.toExpert(): Expert {
    val expert = Expert(name, surname)
    expert.id = id
    return expert
}