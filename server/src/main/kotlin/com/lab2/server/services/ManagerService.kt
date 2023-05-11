package com.lab2.server.services

import com.lab2.server.dto.ManagerDTO

interface ManagerService {

    fun getAll(): List<ManagerDTO>

    fun getManagerById(managerId: Long): ManagerDTO?

    //fun insertManager(manager: ManagerDTO)
}


