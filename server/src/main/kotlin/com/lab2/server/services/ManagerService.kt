package com.lab2.server.services

import com.lab2.server.data.Manager
import com.lab2.server.dto.ManagerDTO

interface ManagerService {
    fun getManager(managerEmail: String): ManagerDTO
    fun unsafeGetManager(managerEmail: String): Manager?

}


