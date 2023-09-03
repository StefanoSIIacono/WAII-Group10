package com.lab2.server.data

import jakarta.persistence.*

@Entity
@Table (name = "tickets")
class Ticket (


    val obj: String,

    @ManyToOne
    var arg: Expertise,

    @Enumerated(value = EnumType.STRING)
    var priority: Priority,

    @ManyToOne
    var profile: Profile,

    @ManyToOne
    var expert: Expert? = null,

    @ManyToOne
    var product: Product,

    var indexE: Int=0,
    var offsetE: Int=0,

    var indexP: Int=0,
    var offsetP: Int=0,

    ): EntityBase<Long>()
{
    @OneToMany(mappedBy = "ticket", cascade = [CascadeType.ALL])
    var statusHistory = mutableListOf<TicketStatus>()

    @OneToMany(mappedBy = "ticket")
    var messages = mutableListOf<Message>()

    fun addMessage(m: Message){
        m.ticket = this
        this.messages.add(m)
    }

    fun removeExpert(){
        this.expert?.inProgressTickets?.remove(this)
        this.expert = null
    }

    fun addStatus(s: TicketStatus){
        //s.ticket = this
        statusHistory.add(s)
    }

    fun addProduct(p: Product){
        p.tickets.add(this)
        this.product = p
    }

    fun addProfile(p: Profile){
        p.tickets.add(this)
        this.profile = p
    }

    fun newPriority(p: Priority) {
        this.priority = p
    }
}

/*
fun TicketDTO.toTicket(): Ticket {
    return Ticket (obj, arg, priority, profile, expert?.toExpert(), product.toProduct())
}
 */
