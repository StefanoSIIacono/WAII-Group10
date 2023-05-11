package com.lab2.server.services

import com.lab2.server.dto.ExpertDTO

interface ExpertService {
    fun getExpertById(expertId: Long): ExpertDTO
}