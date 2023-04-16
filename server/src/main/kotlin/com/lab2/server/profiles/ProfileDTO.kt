package com.lab2.server.profiles


data class ProfileDTO(
    val email:String,
    val name:String,
    val surname:String
)

fun Profile.toDTO(): ProfileDTO {
    return ProfileDTO(email, name, surname)
}