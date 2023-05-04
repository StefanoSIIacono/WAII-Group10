package com.lab2.server.ticketing

import com.lab2.server.products.Product
import com.lab2.server.profiles.Profile
import jakarta.persistence.*

@Entity
@Table (name = "tickets")
class Ticket (

    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val ticketId: Long,
    val obj: String,
    val arg: String,
    var priority: Int,

    @ManyToOne
    @JoinColumn(name="email", nullable = false)
    // AL POSTO DI EMAIL, PERCHE' DOBBIAMO MAPPARE L'ENTITA'
    var profile: Profile,

    @ManyToOne
    @JoinColumn(name="expert_id", nullable = true)
    var expert: Expert? = null,

    // DA VERIFICARE SE VOGLIAMO MANTENERE IL PRODOTTO NEL TICKET
    @ManyToOne
    @JoinColumn(name="product_id", nullable = false)
    var product: Product
    )
{
    @OneToMany(mappedBy = "ticket")
    var statusHistory = mutableListOf<TicketStatus>()

    @OneToMany(mappedBy = "ticket")
    var messages = mutableListOf<Message>()

    fun addMessage(m: Message){
        m.ticket = this
        this.messages.add(m);
    }

    fun addStatus(s: TicketStatus){
        s.ticket = this;
    }

    fun addProduct(p: Product){
        p.tickets.add(this);
        this.product = p;
    }
}