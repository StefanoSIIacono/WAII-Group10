package com.lab2.server.dto

import com.lab2.server.data.Product

data class ProductDTO(
    val productId: String,
    val name:String,
    val brand:String
)

fun Product.toDTO(): ProductDTO {
    return ProductDTO(id, name, brand)
}