package com.lab2.server.data

import com.lab2.server.dto.ExpertDTO
import jakarta.persistence.*

// ------------------------------- EXPERT -------------------------------
@Entity
@Table(name = "experts")
class Expert(
    @Id
    @Column(updatable = false, nullable = false)
    val email: String,
    var name: String,
    var surname: String,
    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST])
    @JoinTable(
        name = "expert_expertise",
        joinColumns = [JoinColumn(name = "expert_email")],
        inverseJoinColumns = [JoinColumn(name = "expertise_field")]
    )
    val expertises: MutableSet<Expertise> = mutableSetOf(),
) {

    @OneToMany(mappedBy = "expert")
    var inProgressTickets: MutableList<Ticket> = mutableListOf()

    @OneToMany(mappedBy = "expert")
    var changedStatuses: MutableList<TicketStatus> = mutableListOf()

    @OneToMany(mappedBy = "expert")
    var messages: MutableSet<Message> = mutableSetOf()
    fun addExpertise(e: Expertise) {
        this.expertises.add(e)
        e.experts.add(this)
    }

    fun removeExpertise(e: Expertise) {
        this.expertises.removeAll { it.field == e.field }
        e.experts.removeAll { it.email == this.email }
    }

}

fun ExpertDTO.toExpert(): Expert {
    return Expert(this.email, this.name, this.surname)
}