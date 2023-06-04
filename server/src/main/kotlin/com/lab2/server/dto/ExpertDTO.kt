package com.lab2.server.dto

import com.lab2.server.data.Expert

data class ExpertDTO(
    val email: String,
    val name: String,
    val surname: String,
    val expertises: MutableSet<ExpertiseDTO> = mutableSetOf(),
    var changedStatuses: MutableList<TicketStatusDTO> = mutableListOf()
)
{
    fun addExpertiseDTO(e: ExpertiseDTO) {
        this.expertises.add(e)
    }
}

data class CreateExpertDTO(
    val email: String,
    val password: String,
    val name: String,
    val surname: String,
    val expertises: MutableSet<String> = mutableSetOf(),
)

fun Expert.toDTO(): ExpertDTO {
    return ExpertDTO(
        this.email,
        this.name,
        this.surname,
        this.expertises.map { it.toDTO()}.toMutableSet()
    )
}

