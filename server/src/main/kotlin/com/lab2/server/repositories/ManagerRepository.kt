package com.lab2.server.repositories

import com.lab2.server.data.Manager
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ManagerRepository : JpaRepository<Manager, String>