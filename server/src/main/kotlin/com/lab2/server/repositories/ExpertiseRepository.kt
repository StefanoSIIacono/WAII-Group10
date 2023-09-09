package com.lab2.server.repositories

import com.lab2.server.data.Expertise
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExpertiseRepository : JpaRepository<Expertise, Long> {

    fun findByField(field: String): Expertise?
}