package com.lab2.server.services

import com.lab2.server.data.Expert
import com.lab2.server.data.Ticket
import com.lab2.server.dto.ExpertDTO
import com.lab2.server.dto.ExpertiseDTO

interface ExpertService {

    fun getAll(): MutableSet<ExpertDTO>
    fun getExpertByEmail(email: String): ExpertDTO?
    fun getExpertisesByExpert(email: String): MutableSet<ExpertiseDTO>
    fun insertExpert(email: String, name: String, surname: String)
    fun addExpertiseToExpert(expert: ExpertDTO, expertise: ExpertiseDTO)

    fun addTicketToExpert(expert: Expert, ticket: Ticket)
}