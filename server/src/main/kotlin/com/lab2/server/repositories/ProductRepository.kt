package com.lab2.server.repositories

import com.lab2.server.data.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, String> {
    fun findByNameContaining(name: String, pageable: Pageable): Page<Product>
}