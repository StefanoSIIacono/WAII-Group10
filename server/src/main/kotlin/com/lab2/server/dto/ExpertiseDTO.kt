package com.lab2.server.dto

import com.lab2.server.data.Expertise
import jakarta.persistence.Id

data class ExpertiseDTO(
    val id: Long?,
    val field: String
)

fun Expertise.toDTO(): ExpertiseDTO {
    return ExpertiseDTO(id, field)
}
