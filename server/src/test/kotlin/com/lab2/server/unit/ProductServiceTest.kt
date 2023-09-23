package com.lab2.server.unit

import com.lab2.server.data.Product
import com.lab2.server.dto.PagedDTO
import com.lab2.server.dto.PagedMetadata
import com.lab2.server.dto.toDTO
import com.lab2.server.exceptionsHandler.exceptions.ProductNotFoundException
import com.lab2.server.repositories.ProductRepository
import com.lab2.server.serviceImpl.ProductServiceImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull

class ProductServiceTest {
    private val repository = mockk<ProductRepository>()

    @Test
    fun getAllTest() {
        // given
        val productPage = PageImpl(
            listOf(
                Product("1", "Shirt", "Tommy Hilfiger"),
                Product("2", "Shoes", "Vans")
            ), PageRequest.of(0, 100, Sort.by("name")), 2
        )
        every { repository.findAll(PageRequest.of(0, 100, Sort.by("name"))) } returns productPage

        val service = ProductServiceImpl(repository)
        // when
        val result = service.getAllPaged(0, 100)

        // then
        verify(exactly = 1) { repository.findAll(PageRequest.of(0, 100, Sort.by("name"))) }
        assertEquals(
            PagedDTO(
                PagedMetadata(
                    productPage.number + 1,
                    productPage.totalPages,
                    productPage.numberOfElements
                ), productPage.content.map { it.toDTO() }), result
        )
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
        try {
            service.getProduct("2")
        } catch (e: ProductNotFoundException) {
            assertEquals("Product not found", e.message)
        }


        // then
        verify(exactly = 1) { repository.findByIdOrNull("2") }
    }

}