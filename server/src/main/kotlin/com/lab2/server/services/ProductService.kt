package com.lab2.server.services

import com.lab2.server.dto.PagedDTO
import com.lab2.server.dto.ProductDTO


interface ProductService {
    fun getAllPaged(page: Int, offset: Int): PagedDTO<ProductDTO>
    fun searchByNamePaged(name: String, page: Int, offset: Int): PagedDTO<ProductDTO>
    fun getProduct(productId: String): ProductDTO
}