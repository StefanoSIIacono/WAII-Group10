package com.lab2.server.serviceImpl

import com.lab2.server.dto.PagedDTO
import com.lab2.server.dto.PagedMetadata
import com.lab2.server.dto.ProductDTO
import com.lab2.server.dto.toDTO
import com.lab2.server.exceptionsHandler.exceptions.ProductNotFoundException
import com.lab2.server.repositories.ProductRepository
import com.lab2.server.services.ProductService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(private val productRepository: ProductRepository) : ProductService {
    override fun getAllPaged(page: Int, offset: Int): PagedDTO<ProductDTO> {
        val pageResult = productRepository.findAll(PageRequest.of(page, offset, Sort.by("name")))
        val meta = PagedMetadata(pageResult.number + 1, pageResult.totalPages, pageResult.numberOfElements)

        return PagedDTO(meta, pageResult.content.map { it.toDTO() })
    }

    override fun getProduct(productId: String): ProductDTO {
        return productRepository.findByIdOrNull(productId)?.toDTO()
            ?: throw ProductNotFoundException("Product not found")
    }
}