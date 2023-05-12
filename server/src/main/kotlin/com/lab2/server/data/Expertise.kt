package com.lab2.server.data

import com.lab2.server.dto.ExpertiseDTO
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table

@Entity
@Table(name = "expertises")
class Expertise (
    @Column(unique = true)
    val field: String
): EntityBase<Long>()
{
    @ManyToMany(mappedBy = "expertises")
    val experts: MutableSet<Expert> = mutableSetOf()
    /*fun addExpert(e: Expert) {
        experts.add(e)
        e.expertises.add(this)
    }*/
}

fun ExpertiseDTO.toExpertise(): Expertise {
    val expertise = Expertise(field)
    expertise.id = id
    return expertise
}