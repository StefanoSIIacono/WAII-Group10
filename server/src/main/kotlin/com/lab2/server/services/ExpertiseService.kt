package com.lab2.server.services

import com.lab2.server.dto.ExpertDTO
import com.lab2.server.dto.ExpertiseDTO

interface ExpertiseService {

    fun getAll(): List<ExpertiseDTO>
    fun getExpertsByExpertise(field: String): List<ExpertDTO>
    fun getExpertise(field: String): ExpertiseDTO?
    fun createExpertise(field: String)
    fun deleteExpertise(expertise: String)

}
