package com.lab2.server.serviceImpl

import com.lab2.server.dto.ExpertDTO
import com.lab2.server.dto.toDTO
import com.lab2.server.exceptionsHandler.exceptions.ExpertNotFoundException
import com.lab2.server.repositories.ExpertRepository
import com.lab2.server.services.ExpertService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ExpertServiceImpl(private val expertRepository: ExpertRepository): ExpertService {
    override fun getExpertById(expertId: Long): ExpertDTO {
        return expertRepository.findByIdOrNull(expertId)?.toDTO() ?: throw ExpertNotFoundException("Expert not found")
    }
}