package com.lab2.server.services

import com.lab2.server.dto.*
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

interface SecurityService {
    fun signup(profile: CreateProfileDTO)
    fun createExpert(expert: CreateExpertDTO)
    fun login(login: LoginDTO): TokenDTO
    fun getLoggedUserInfo(principal: JwtAuthenticationToken): MeDTO
}