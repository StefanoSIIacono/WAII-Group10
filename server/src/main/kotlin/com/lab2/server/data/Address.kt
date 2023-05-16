package com.lab2.server.data

import com.lab2.server.dto.AddressDTO
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "addresses")
class Address (
    val city: String,
    val country: String,
    val zipCode: String,
    val street: String,
    val houseNumber: String,

    @OneToOne
    var profile: Profile,
    @Id var id: Long? = null
)
{
}

fun AddressDTO.toAddress(): Address {
    val address = Address(city, country, zipCode, street, houseNumber, profile.toProfile())
    address.id = id
    return address
}