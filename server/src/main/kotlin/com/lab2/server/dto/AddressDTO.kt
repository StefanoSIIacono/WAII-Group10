package com.lab2.server.dto

import com.lab2.server.data.Address

data class AddressDTO (
    val id: String?,
    val city: String,
    val country: String,
    val zipCode: String,
    val street: String,
    val houseNumber: String,
    val profile: ProfileDTO?
)
{
}
// FOR THE GET WE WANT TO DELETE THE PROFILE WHEN SHOWN TO THE CLIENT
data class GetAddressDTO (
        val city: String,
        val country: String,
        val zipCode: String,
        val street: String,
        val houseNumber: String,
)

fun Address.toDTO(): GetAddressDTO {
    return GetAddressDTO(
                        this.city,
                        this.country,
                        this.zipCode,
                        this.street,
                        this.houseNumber
                        );
}