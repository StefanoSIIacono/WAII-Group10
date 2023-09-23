package com.lab2.server.data

import com.lab2.server.dto.ProductDTO
import jakarta.persistence.*

@Entity
@Table(name = "products")
class Product(
    @Id
    @Column(updatable = false, nullable = false)
    var id: String,
    var name: String,
    var brand: String
) {
    @OneToMany(mappedBy = "product")
    val tickets: MutableList<Ticket> = mutableListOf()
}

fun ProductDTO.toProduct(): Product {
    return Product(this.productId, this.name, this.brand)
}