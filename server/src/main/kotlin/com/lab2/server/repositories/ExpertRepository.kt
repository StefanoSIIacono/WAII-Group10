package com.lab2.server.repositories

import com.lab2.server.data.Expert
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExpertRepository: JpaRepository<Expert, Long> {
}