package com.lab2.server.services

import com.lab2.server.dto.ProfileDTO

interface ProfileService {
    fun getAll(): List<ProfileDTO>
    fun getProfileByEmail(email:String): ProfileDTO?
    fun insertProfile(profile: ProfileDTO)
    fun editProfile(email:String, profile: ProfileDTO)
}