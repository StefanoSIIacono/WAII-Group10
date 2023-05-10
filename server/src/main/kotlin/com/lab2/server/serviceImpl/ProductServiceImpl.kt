package com.lab2.server.serviceImpl

import com.lab2.server.dto.ProductDTO
import com.lab2.server.dto.toDTO
import com.lab2.server.repositories.ProductRepository
import com.lab2.server.services.ProductService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(private val productRepository: ProductRepository): ProductService {
    override fun getAll(): List<ProductDTO> {
        return productRepository.findAll().map { it.toDTO() }
    }

    override fun getProduct(productId: String): ProductDTO? {
        return productRepository.findByIdOrNull(productId)?.toDTO()
    }
}