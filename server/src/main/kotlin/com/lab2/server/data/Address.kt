package com.lab2.server.data

import com.lab2.server.dto.AddressDTO
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "addresses")
class Address(
    val address: String,
    val zipCode: String,
    val city: String,
    val country: String
) : EntityBase<Long>() {
    @OneToOne(fetch = FetchType.LAZY)
    lateinit var profile: Profile
}

fun AddressDTO.toAddress(): Address {
    return Address(
        this.address,
        this.zipCode,
        this.city,
        this.country
    )
}