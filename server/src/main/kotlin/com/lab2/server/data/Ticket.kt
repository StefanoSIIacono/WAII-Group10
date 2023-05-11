package com.lab2.server.data

import jakarta.persistence.*

@Entity
@Table (name = "tickets")
class Ticket (

    val obj: String,
    val arg: String,
    @Enumerated(value = EnumType.STRING)
    var priority: Priority,

    @ManyToOne
    // AL POSTO DI EMAIL, PERCHE' DOBBIAMO MAPPARE L'ENTITA'
    var profile: Profile,

    @ManyToOne
    var expert: Expert? = null,

    // DA VERIFICARE SE VOGLIAMO MANTENERE IL PRODOTTO NEL TICKET
    @ManyToOne
    var product: Product

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

    fun addStatus(s: TicketStatus){
        s.ticket = this
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
}