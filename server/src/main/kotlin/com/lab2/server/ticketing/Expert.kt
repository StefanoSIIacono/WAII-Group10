package com.lab2.server.ticketing

import com.lab2.server.Chatter
import com.lab2.server.EntityBase
import com.lab2.server.StatusChanger
import jakarta.persistence.*
import org.aspectj.weaver.IntMap
import javax.management.monitor.StringMonitor

// ------------------------------- EXPERT -------------------------------
@Entity
@Table (name = "experts")
class Expert(
    var name: String,
    var surname: String,

    ): EntityBase<Long>(),  StatusChanger, Chatter
{
    @OneToMany(mappedBy = "expert")
    var inProgressTickets = mutableListOf<Ticket>();

    @OneToMany(mappedBy = "expert")
    var changedStatuses = mutableListOf<TicketStatus>();

    @ManyToMany
    @JoinTable(name = "expert_expertise",
        joinColumns = [JoinColumn(name="expert_id")],
        inverseJoinColumns = [JoinColumn(name = "expertise_id")]
    )
    val expertises: MutableSet<Expertise> = mutableSetOf();

    fun assignTicket(t: Ticket){
        t.expert = this;
        this.inProgressTickets.add(t);
    }

    fun addTicketStatus(s: TicketStatus){
        s.expert = this;
        this.addTicketStatus(s);
    }

    fun addExpertise(e: Expertise) {
        expertises.add(e)
        e.experts.add(this)
    }

    override fun changeStatus() {
        //
    }

    override fun write() {
        //
    }
}

fun ExpertDTO.toExpert(): Expert {
    return Expert(name, surname)
}


// ----------------------------------- EXPERTISE ----------------------------------------------
@Entity
@Table(name = "expertises")
class Expertise (
    val field: String
): EntityBase<Long>()
{
    @ManyToMany(mappedBy = "expertises")
    val experts: MutableSet<Expert> = mutableSetOf()
    fun addExpert(e: Expert) {
        experts.add(e)
        e.expertises.add(this)
    }
}

fun ExpertiseDTO.toExpertise(): Expertise {
    return Expertise(field)
}

data class ExpertiseDTO(
    val field: String
)

fun Expertise.toDTO(): ExpertiseDTO {
    return ExpertiseDTO(field)
}

