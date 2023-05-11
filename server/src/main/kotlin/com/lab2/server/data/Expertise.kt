package com.lab2.server.data

import com.lab2.server.dto.ExpertiseDTO
import jakarta.persistence.Entity
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table

@Entity
@Table(name = "expertises")
class Expertise (
    val field: String
): EntityBase<Long>()
{
    @ManyToMany(mappedBy = "expertises")
    val experts: MutableSet<Expert> = mutableSetOf()
    fun addExpert(e: Expert) {
        experts.add(e)
        e.expertises.add(this)
    }
}

fun ExpertiseDTO.toExpertise(): Expertise {
    return Expertise(field)
}