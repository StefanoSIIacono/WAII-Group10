package com.lab2.server.dto

import com.lab2.server.data.Expert
import com.lab2.server.data.TicketStatus

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
    return ExpertDTO(getId(), name, surname, expertises.map { it.toDTO()}.toMutableSet())
}

