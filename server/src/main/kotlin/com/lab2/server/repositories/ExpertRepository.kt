package com.lab2.server.repositories

import com.lab2.server.data.Expert
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExpertRepository : JpaRepository<Expert, String> {
    fun findByEmailContaining(email: String, pageable: Pageable): Page<Expert>
    fun findByEmailContainingAndExpertisesField(
        email: String,
        expertisesField: String,
        pageable: Pageable
    ): Page<Expert>
}