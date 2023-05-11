package com.lab2.server.serviceImpl


import com.lab2.server.dto.ExpertDTO
import com.lab2.server.data.Expertise
import com.lab2.server.dto.ExpertiseDTO
import com.lab2.server.dto.toDTO
import com.lab2.server.repositories.ExpertiseRepository
import com.lab2.server.services.ExpertiseService
import org.springframework.stereotype.Service

@Service
class ExpertiseServiceImpl (private val expertiseRepository: ExpertiseRepository): ExpertiseService {

    override fun getExpertsByExpertise(field: String): List<ExpertDTO>{
        return expertiseRepository.findByField(field).experts.map { it.toDTO() }
    }

    override fun getAll(): List<ExpertiseDTO>{
        return expertiseRepository.findAll().map { it.toDTO() }
    }

    override fun getExpertise(field: String): ExpertiseDTO? {
        return expertiseRepository.findByField(field).toDTO()
    }

    override fun createExpertise(expertise: String) {
        expertiseRepository.save(Expertise(expertise))
    }
    override fun deleteExpertise(expertise: String){
        val expertiseEntity = expertiseRepository.findByField(expertise)
        expertiseRepository.delete(expertiseEntity)
    }
}