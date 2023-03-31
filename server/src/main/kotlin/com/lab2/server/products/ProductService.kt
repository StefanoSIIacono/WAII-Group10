package com.lab2.server.products


interface ProductService {
    fun getAll(): List<ProductDTO>
    fun getProduct(productId:String): ProductDTO?
}