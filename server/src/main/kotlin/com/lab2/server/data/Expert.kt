package com.lab2.server.data

import com.lab2.server.dto.ExpertDTO
import jakarta.persistence.*

// ------------------------------- EXPERT -------------------------------
@Entity
@Table (name = "experts")
class Expert(
        @Id
        @Column(updatable = false, nullable = false)
        val email: String,
        var name: String,
        var surname: String,
)
{
    @ManyToMany
    @JoinTable(name = "expert_expertise",
            joinColumns = [JoinColumn(name="expert_email")],
            inverseJoinColumns = [JoinColumn(name = "expertise_id")]
    )
    val expertises: MutableSet<Expertise> = mutableSetOf()

    @OneToMany(mappedBy = "expert")
    var inProgressTickets = mutableListOf<Ticket>()

    @OneToMany(mappedBy = "expert")
    var changedStatuses = mutableListOf<TicketStatus>()

    @OneToMany( mappedBy = "expert")
    var messages: MutableSet<Message> = mutableSetOf()

    fun addTicket(t: Ticket){
        t.expert = this
        this.inProgressTickets.add(t)
    }


    fun addTicketStatus(s: TicketStatus){
        s.expert = this
        this.changedStatuses.add(s)
    }

    fun addExpertise(e: Expertise) {
        this.expertises.add(e)
        e.experts.add(this)
    }

}

fun ExpertDTO.toExpert(): Expert {
    return Expert(this.email, this.name, this.surname)
}