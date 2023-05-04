package com.lab2.server.products

import com.lab2.server.ticketing.Ticket
import jakarta.persistence.*

@Entity
@Table(name = "products")
class Product (
    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: String,
    var name: String,
    var brand: String
)
// PARTE AGGIUNTA DA LAB3, TUTTAVIA FORSE E' MEGLIO TOGLIERE IL PRODOTTO DAL TICKET
{

    @OneToMany(mappedBy = "ticket")
    var tickets = mutableListOf<Ticket>()
}