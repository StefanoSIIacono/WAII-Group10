package com.lab2.server.services

import com.lab2.server.dto.ProfileDTO
import com.lab2.server.dto.TicketDTO

interface ProfileService {
    fun getAll(): List<ProfileDTO>
    fun getProfileByEmail(email:String): ProfileDTO?
    fun getTicketsByEmail(email: String): MutableList<TicketDTO>
    fun insertProfile(profile: ProfileDTO)
    fun editProfile(email:String, profile: ProfileDTO)
}