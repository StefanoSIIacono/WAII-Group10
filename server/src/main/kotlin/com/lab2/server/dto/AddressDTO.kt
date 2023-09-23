package com.lab2.server.dto

import com.lab2.server.data.Address

data class AddressDTO(
    val address: String,
    val zipCode: String,
    val city: String,
    val country: String,
)

fun Address.toDTO(): AddressDTO {
    return AddressDTO(
        this.address,
        this.zipCode,
        this.city,
        this.country
    )
}