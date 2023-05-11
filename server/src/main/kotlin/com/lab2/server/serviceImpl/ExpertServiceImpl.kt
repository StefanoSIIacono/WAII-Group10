package com.lab2.server.serviceImpl

import com.lab2.server.data.toExpert
import com.lab2.server.data.toExpertise
import com.lab2.server.dto.ExpertDTO
import com.lab2.server.dto.ExpertiseDTO
import com.lab2.server.dto.toDTO
import com.lab2.server.exceptionsHandler.exceptions.ExpertNotFoundException
import com.lab2.server.repositories.ExpertRepository
import com.lab2.server.services.ExpertService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ExpertServiceImpl(private val expertRepository: ExpertRepository): ExpertService {

    override fun getAll(): MutableSet<ExpertDTO> {
        return expertRepository.findAll().map { it.toDTO() }.toMutableSet()
    }
    override fun getExpertById(expertId: Long): ExpertDTO? {
        return expertRepository.findByIdOrNull(expertId)?.toDTO() ?: throw ExpertNotFoundException("Expert not found")
    }

    override fun getExpertisesByExpert(expertId: Long): MutableSet<ExpertiseDTO> {
        return expertRepository.findByIdOrNull(expertId)?.toDTO()?.expertises ?: throw ExpertNotFoundException("Expert not found")
    }

    override fun addExpertiseToExpert(expert: ExpertDTO, expertise: ExpertiseDTO) {
        //val expertId = expert.id
        expert.addExpertiseDTO(expertise)
        val exp = expert.toExpert()
        expertRepository.save(exp)
    }
}