package com.lab2.server.data

import com.lab2.server.dto.ExpertiseDTO
import jakarta.persistence.*

@Entity
@Table(name = "expertises")
class Expertise(
    @Id
    @Column(updatable = false, nullable = false)
    val field: String
) {
    @ManyToMany(mappedBy = "expertises")
    val experts: MutableSet<Expert> = mutableSetOf()
}

fun ExpertiseDTO.toExpertise(): Expertise {
    return Expertise(this.field)
}