package com.lab2.server.dto

import com.lab2.server.data.Expert

data class ExpertDTO(
    val id: Long?,
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
fun Expert.toDTO(): ExpertDTO {
    return ExpertDTO(this.id, this.name, this.surname, this.expertises.map { it.toDTO()}.toMutableSet())
}

