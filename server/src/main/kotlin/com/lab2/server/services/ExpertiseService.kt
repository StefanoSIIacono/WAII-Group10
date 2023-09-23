package com.lab2.server.services

import com.lab2.server.data.Expertise
import com.lab2.server.dto.ExpertDTO
import com.lab2.server.dto.ExpertiseDTO
import com.lab2.server.dto.PagedDTO

interface ExpertiseService {

    fun getAllPaginated(page: Int, offset: Int): PagedDTO<ExpertiseDTO>
    fun searchByFieldPaginated(field: String, page: Int, offset: Int): PagedDTO<ExpertiseDTO>
    fun getExpertsByExpertisePaginated(field: String, page: Int, offset: Int): PagedDTO<ExpertDTO>
    fun getExpertise(field: String): ExpertiseDTO
    fun createExpertise(field: String)
    fun deleteExpertise(expertise: String)
    fun unsafeGetExpertise(expertise: String): Expertise?
}
