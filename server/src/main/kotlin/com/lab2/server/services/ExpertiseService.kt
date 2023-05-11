package com.lab2.server.services

import com.lab2.server.dto.ExpertDTO
import com.lab2.server.dto.ExpertiseDTO

interface ExpertiseService {

    fun getExpertsByExpertise(field: String): List<ExpertDTO>

    fun getAll(): List<ExpertiseDTO>

    fun getExpertise(field: String): ExpertiseDTO?
}
