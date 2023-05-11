package com.lab2.server.dto

import com.lab2.server.data.Expert

data class ExpertDTO(
    val id: Long?,
    val name: String,
    val surname: String,
    val expertises: MutableSet<ExpertiseDTO> = mutableSetOf()
)

fun Expert.toDTO(): ExpertDTO {
    return ExpertDTO(id, name, surname, expertises.map { it.toDTO()}.toMutableSet())
}