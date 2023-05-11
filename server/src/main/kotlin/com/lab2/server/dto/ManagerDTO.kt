package com.lab2.server.dto

import com.lab2.server.data.Manager
import com.lab2.server.data.TicketStatus

class ManagerDTO (
    var name: String,
    var surname: String
)
{
}

fun Manager.toDTO(): ManagerDTO {
    return ManagerDTO(name, surname)
}