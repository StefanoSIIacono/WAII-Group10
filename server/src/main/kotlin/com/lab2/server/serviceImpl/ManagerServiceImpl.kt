package com.lab2.server.serviceImpl

import com.lab2.server.data.Manager
import com.lab2.server.dto.ManagerDTO
import com.lab2.server.dto.toDTO
import com.lab2.server.exceptionsHandler.exceptions.ManagerNotFoundException
import com.lab2.server.repositories.ManagerRepository
import com.lab2.server.services.ManagerService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ManagerServiceImpl(private val managerRepository: ManagerRepository) : ManagerService {

    override fun getManager(managerEmail: String): ManagerDTO {
        return managerRepository.findByIdOrNull(managerEmail)?.toDTO()
            ?: throw ManagerNotFoundException("Manager not found")
    }

    override fun unsafeGetManager(managerEmail: String): Manager? {
        return managerRepository.findByIdOrNull(managerEmail)
    }

}