package com.lab2.server.data

import com.lab2.server.dto.ProductDTO
import jakarta.persistence.*

@Entity
@Table(name = "products")
class Product (
    @Id
    @Column(updatable = false, nullable = false)
    var id: String,
    var name: String,
    var brand: String
)
// PARTE AGGIUNTA DA LAB3, TUTTAVIA FORSE E' MEGLIO TOGLIERE IL PRODOTTO DAL TICKET
{
    @OneToMany(mappedBy = "product")
    val tickets = mutableListOf<Ticket>()

    fun addTicket(t: Ticket){
        t.product = this
        this.tickets.add(t)
    }
}

fun ProductDTO.toProduct(): Product {
    return Product(productId, name, brand)
}