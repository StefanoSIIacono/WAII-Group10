package com.lab2.server.repositories

import com.lab2.server.data.Manager
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository

@Repository
interface ManagerRepository: JpaRepository<Manager, Long> {
}