package com.lab2.server.dto

import com.lab2.server.data.Manager

class ManagerDTO(
    val email: String,
    var name: String,
    var surname: String
)

fun Manager.toDTO(): ManagerDTO {
    return ManagerDTO(
        this.email,
        this.name,
        this.surname
    )
}