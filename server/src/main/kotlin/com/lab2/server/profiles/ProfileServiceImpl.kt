package com.lab2.server.profiles

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProfileServiceImpl(private val profileRepository: ProfileRepository): ProfileService {
    override fun getAll(): List<ProfileDTO> {
        return profileRepository.findAll().map { it.toDTO() }
    }

    override fun getProduct(profileId: String): ProfileDTO? {
        return profileRepository.findByIdOrNull(profileId)?.toDTO()
    }
}