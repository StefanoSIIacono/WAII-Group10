package com.lab2.server.services

import com.lab2.server.data.Expert
import com.lab2.server.dto.ExpertDTO
import com.lab2.server.dto.PagedDTO
import com.lab2.server.dto.StatsDTO

interface ExpertService {

    fun getAllPaginated(page: Int, offset: Int): PagedDTO<ExpertDTO>
    fun searchByEmailAndExpertisePaginated(
        email: String,
        expertise: String?,
        page: Int,
        offset: Int
    ): PagedDTO<ExpertDTO>

    fun getExpertByEmail(email: String): ExpertDTO
    fun insertExpert(expert: ExpertDTO)
    fun addExpertiseToExpert(expertEmail: String, expertise: String)
    fun removeExpertiseFromExpert(expertEmail: String, expertise: String)
    fun getExpertStats(email: String): StatsDTO
    fun unsafeGetExpertByEmail(email: String): Expert?
}