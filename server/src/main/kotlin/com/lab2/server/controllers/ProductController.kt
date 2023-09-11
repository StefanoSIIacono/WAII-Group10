package com.lab2.server.controllers

import com.lab2.server.dto.PagedDTO
import com.lab2.server.dto.ProductDTO
import com.lab2.server.services.ProductService
import io.micrometer.observation.annotation.Observed
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import lombok.extern.slf4j.Slf4j
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@Observed
@Slf4j
class ProductController(private val productService: ProductService) {

    @GetMapping("/products")
    @ResponseStatus(HttpStatus.OK)
    fun getAllPaginated(
        @RequestParam(required = false, defaultValue = "1") @Min(1) page: Int,
        @RequestParam(required = false, defaultValue = "100") @Min(1) @Max(100) offset: Int,
    ): PagedDTO<ProductDTO> {
        log.info("Retrieving all products")
        return productService.getAllPaged(page - 1, offset)
    }

    @GetMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.OK)
    fun getProduct(@PathVariable productId: String): ProductDTO {
        log.info("Retrieving product $productId")
        return productService.getProduct(productId)
    }
}