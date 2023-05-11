package com.lab2.server.services

import com.lab2.server.data.Expert
import com.lab2.server.data.Expertise
import com.lab2.server.dto.ExpertDTO
import com.lab2.server.dto.ExpertiseDTO

interface ExpertService {

    fun getAll(): MutableSet<ExpertDTO>
    fun getExpertById(expertId: Long): ExpertDTO?
    fun getExpertisesByExpert(expertId: Long): MutableSet<ExpertiseDTO>
    fun addExpertiseToExpert(expert: ExpertDTO, expertise: ExpertiseDTO)
}