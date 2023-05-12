package com.lab2.server.dto

import com.lab2.server.data.Manager

class ManagerDTO (
    val id: Long?,
    var name: String,
    var surname: String
)
{
}

fun Manager.toDTO(): ManagerDTO {
    return ManagerDTO(id, name, surname)
}