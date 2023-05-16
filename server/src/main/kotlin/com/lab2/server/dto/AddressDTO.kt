package com.lab2.server.dto

import com.lab2.server.data.Address

data class AddressDTO (
    val id: Long?,
    val city: String,
    val country: String,
    val zipCode: String,
    val street: String,
    val houseNumber: String,
    val profile: ProfileDTO
)
{
}

/*data class ChangeProfileAddressDTO(
    val city: String,
    val country: String,
    val zipCode: String,
    val street: String,
    val houseNumber: String,
    val profile: String
)*/

data class CreateOrChangeProfileAddressDTO(
    val city: String,
    val country: String,
    val zipCode: String,
    val street: String,
    val houseNumber: String,
)

fun Address.toDTO(): AddressDTO {
    return AddressDTO(id, city, country, zipCode, street, houseNumber, profile.toDTO());
}