package com.lab2.server.services

import com.lab2.server.dto.ExpertDTO
import com.lab2.server.dto.ExpertiseDTO
import com.lab2.server.dto.PagedDTO

interface ExpertiseService {

    fun getAllPaginated(page: Int, offset: Int): PagedDTO<ExpertiseDTO>
    fun getExpertsByExpertise(field: String): List<ExpertDTO>
    fun getExpertise(field: String): ExpertiseDTO
    fun createExpertise(field: String)
    fun deleteExpertise(expertise: String)

}
