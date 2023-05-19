package com.lab2.server.dto

import com.lab2.server.data.Profile


data class ProfileDTO(
    val email:String,
    val name:String,
    val surname:String,
    val address: GetAddressDTO?
)

data class ChangeProfileInfoDTO(
    val name:String,
    val surname:String
)

fun Profile.toDTO(): ProfileDTO {
    return ProfileDTO(   this.email,
                            this.name,
                            this.surname,
                            this.address?.toDTO()
    )
}

