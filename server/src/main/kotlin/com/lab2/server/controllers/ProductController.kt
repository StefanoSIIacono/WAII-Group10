package com.lab2.server.controllers

import com.lab2.server.dto.ProductDTO
import com.lab2.server.exceptionsHandler.exceptions.ProductNotFoundException
import com.lab2.server.services.ProductService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController(private val productService: ProductService) {
    @GetMapping("/products/")
    @ResponseStatus(HttpStatus.OK)
    fun getAll(): List<ProductDTO>{
        return productService.getAll()
    }
    @GetMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.OK)
    fun getProduct(@PathVariable productId:String): ProductDTO {
        return productService.getProduct(productId)
            ?: throw ProductNotFoundException("Product not found")
    }
}