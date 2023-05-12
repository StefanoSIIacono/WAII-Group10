package com.lab2.server.serviceImpl


import com.lab2.server.dto.ExpertDTO
import com.lab2.server.data.Expertise
import com.lab2.server.data.toExpertise
import com.lab2.server.dto.ExpertiseDTO
import com.lab2.server.dto.toDTO
import com.lab2.server.exceptionsHandler.exceptions.DuplicatedExpertiseException
import com.lab2.server.exceptionsHandler.exceptions.ExpertiseNotFoundException
import com.lab2.server.repositories.ExpertiseRepository
import com.lab2.server.services.ExpertiseService
import org.springframework.stereotype.Service

@Service
class ExpertiseServiceImpl (private val expertiseRepository: ExpertiseRepository): ExpertiseService {

    override fun getExpertsByExpertise(field: String): List<ExpertDTO>{
        val exp = expertiseRepository.findByField(field)?.toDTO() ?:throw DuplicatedExpertiseException("Expertise exists!")
        return exp.toExpertise().experts.map { it.toDTO() }
    }

    override fun getAll(): List<ExpertiseDTO>{
        return expertiseRepository.findAll().map { it.toDTO() }
    }

    override fun getExpertise(field: String): ExpertiseDTO? {
        return expertiseRepository.findByField(field)?.toDTO()
    }

    override fun createExpertise(field: String) {
        expertiseRepository.findByField(field)?.toDTO() ?: throw DuplicatedExpertiseException("Expertise exists!")
        expertiseRepository.save(Expertise(field))
    }
    override fun deleteExpertise(expertise: String){
        val expertiseEntity = expertiseRepository.findByField(expertise) ?: throw ExpertiseNotFoundException("Expertise not found")

        expertiseRepository.delete(expertiseEntity)
    }
}