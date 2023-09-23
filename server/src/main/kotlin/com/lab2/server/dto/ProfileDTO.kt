package com.lab2.server.dto

import com.lab2.server.data.Profile


data class ProfileDTO(
    val email: String,
    val name: String,
    val surname: String,
    val address: AddressDTO
)

data class ChangeProfileInfoDTO(
    val name: String?,
    val surname: String?,
    val address: AddressDTO?
)

data class CreateProfileDTO(
    val email: String,
    val password: String,
    val name: String,
    val surname: String,
    val address: AddressDTO
)

fun Profile.toDTO(): ProfileDTO {
    return ProfileDTO(
        this.email,
        this.name,
        this.surname,
        this.address.toDTO()
    )
}

