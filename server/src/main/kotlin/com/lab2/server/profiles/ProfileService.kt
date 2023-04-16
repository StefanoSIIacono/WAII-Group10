package com.lab2.server.profiles

interface ProfileService {
    fun getAll(): List<ProfileDTO>
    fun getProfileByEmail(email:String): ProfileDTO?
    fun insertProfile(profile: ProfileDTO)
    fun editProfile(email:String, profile: ProfileDTO)
}