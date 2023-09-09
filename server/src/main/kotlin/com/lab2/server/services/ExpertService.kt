package com.lab2.server.services

import com.lab2.server.data.Expert
import com.lab2.server.dto.ExpertDTO
import com.lab2.server.dto.PagedDTO

interface ExpertService {

    fun getAllPaginated(page: Int, offset: Int): PagedDTO<ExpertDTO>
    fun getExpertByEmail(email: String): ExpertDTO
    fun insertExpert(expert: ExpertDTO)
    fun addExpertiseToExpert(expertEmail: String, expertise: String)
    fun removeExpertiseFromExpert(expertEmail: String, expertise: String)
    fun unsafeGetExpertByEmail(email: String): Expert?
}