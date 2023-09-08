package com.lab2.server.dto


data class PagedDTO<T> (
    val meta: PagedMetadata,
    val data: List<T>,
)

data class PagedMetadata (
    val currentPage: Int,
    val totalPages: Int,
    val totalElements: Int,
)