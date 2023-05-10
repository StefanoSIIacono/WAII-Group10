package com.lab2.server.products

import com.lab2.server.dto.toDTO
import com.lab2.server.data.Product
import com.lab2.server.repositories.ProductRepository
import com.lab2.server.serviceImpl.ProductServiceImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull

class ProductServiceTest {
    private val repository = mockk<ProductRepository>()

    @Test
    fun getAllTest() {
        // given
        val productList = mutableListOf(
            Product("1", "Shirt", "Tommy Hilfiger"),
            Product("2", "Shoes", "Vans")
        )
        every { repository.findAll() } returns productList

        val service = ProductServiceImpl(repository)
        // when
        val result = service.getAll()

        // then
        verify(exactly = 1) { repository.findAll() }
        assertEquals(productList.map { it.toDTO() }, result)
    }

    @Test
    fun getProductByIdTest() {
        // given
        val product = Product("1", "Shirt", "Tommy Hilfiger")
        every { repository.findByIdOrNull("1") } returns product

        val service = ProductServiceImpl(repository)
        // when
        val result = service.getProduct("1")

        // then
        verify(exactly = 1) { repository.findByIdOrNull("1") }
        assertEquals(product.toDTO(), result)
    }

    @Test
    fun getNotExistingProductByIdTest() {
        // given
        every { repository.findByIdOrNull("2") } returns null

        val service = ProductServiceImpl(repository)
        // when
        val result = service.getProduct("2")

        // then
        verify(exactly = 1) { repository.findByIdOrNull("2") }
        assertEquals(null, result)
    }

}