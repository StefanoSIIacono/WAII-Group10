package com.lab2.server.serviceImpl

import com.lab2.server.dto.ManagerDTO
import com.lab2.server.dto.toDTO
import com.lab2.server.exceptionsHandler.exceptions.ManagerNotFoundException
import com.lab2.server.repositories.ManagerRepository
import com.lab2.server.services.ManagerService
import org.springframework.stereotype.Service
import org.springframework.data.repository.findByIdOrNull

@Service
class ManagerServiceImpl (private val managerRepository: ManagerRepository): ManagerService{

    override fun getAll(): List<ManagerDTO> {
        return managerRepository.findAll().map{ it.toDTO() }
    }

    override fun getManagerById(managerId: Long): ManagerDTO? {
        return managerRepository.findByIdOrNull(managerId)?.toDTO() ?: throw ManagerNotFoundException("Manager not found")
    }

}