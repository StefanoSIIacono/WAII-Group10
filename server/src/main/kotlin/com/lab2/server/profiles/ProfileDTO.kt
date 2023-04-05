package com.lab2.server.profiles


data class ProfileDTO(
    val email:String,
    val password:String
)

fun Profile.toDTO(): ProfileDTO {
    return ProfileDTO(email, password)
}