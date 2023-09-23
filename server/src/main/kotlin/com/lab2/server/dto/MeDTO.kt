package com.lab2.server.dto

import com.lab2.server.data.Roles

data class MeDTO(
    val email: String,
    val role: Roles,
    val name: String,
    val surname: String,
    val expertises: MutableSet<ExpertiseDTO>? = null,
    val address: AddressDTO? = null
)