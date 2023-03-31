package com.lab2.server.profiles


data class ProfileDTO(
    val profileId: String,
    val email:String,
    val password:String
)

fun Profile.toDTO(): ProfileDTO {
    return ProfileDTO(id, email, password)
}