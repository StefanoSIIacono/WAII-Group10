package com.lab2.server.data

import com.lab2.server.dto.ExpertiseDTO
import jakarta.persistence.*

@Entity
@Table(name = "expertises")
class Expertise (
    @Column(unique = true)
    val field: String


): EntityBase<Long>()
{
    @OneToMany(mappedBy = "arg")
    val tickets: MutableSet<Ticket> = mutableSetOf()

    @ManyToMany(mappedBy = "expertises")
    val experts: MutableSet<Expert> = mutableSetOf()

    fun addExpert(e: Expert) {
        this.experts.add(e)
        e.expertises.add(this)
    }

    fun addTicket(t:Ticket){
        this.tickets.add(t)
        t.arg=this
    }
}

fun ExpertiseDTO.toExpertise(): Expertise {
    val expertise = Expertise(this.field)
    expertise.id = this.id
    return expertise
}