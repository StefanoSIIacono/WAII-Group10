package com.lab2.server.dto

import com.lab2.server.data.Expert
import com.lab2.server.data.ExpertiseDTO
import com.lab2.server.data.toDTO

data class ExpertDTO(
    val name: String,
    val surname: String,
    val expertises: MutableSet<ExpertiseDTO> = mutableSetOf()
)

fun Expert.toDTO(): ExpertDTO {
    return ExpertDTO(name, surname, expertises.map { it.toDTO()}.toMutableSet())
}