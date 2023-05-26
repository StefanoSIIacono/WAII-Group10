package com.lab2.server.data

import com.lab2.server.dto.AddressDTO
import jakarta.persistence.*

@Entity
@Table(name = "addresses")
class Address (
    val city: String,
    val country: String,
    val zipCode: String,
    val street: String,
    val houseNumber: String,

    @OneToOne (fetch = FetchType.LAZY)
    var profile: Profile,
    @Id @Column(name = "address_id") var id: String? = null
)

fun AddressDTO.toAddress(): Address {
    return Address(
            this.city,
            this.country,
            this.zipCode,
            this.street,
            this.houseNumber,
            this.profile!!.toProfile(),
            this.id)
}

