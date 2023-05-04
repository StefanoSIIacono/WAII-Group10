package com.lab2.server.products

import com.lab2.server.ticketing.Ticket
import jakarta.persistence.*

@Entity
@Table(name = "products")
class Product (
    @Id
    @Column(updatable = false, nullable = false)
    val id: String,
    var name: String,
    var brand: String
)
// PARTE AGGIUNTA DA LAB3, TUTTAVIA FORSE E' MEGLIO TOGLIERE IL PRODOTTO DAL TICKET
{
    @OneToMany(mappedBy = "product")
    val tickets = mutableListOf<Ticket>()

    fun addTicket(t: Ticket){
        t.product = this;
        this.tickets.add(t);
    }
}