package com.lab2.server.services

import com.lab2.server.dto.ProductDTO


interface ProductService {
    fun getAll(): List<ProductDTO>
    fun getProduct(productId:String): ProductDTO?
}