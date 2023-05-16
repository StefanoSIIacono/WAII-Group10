package com.lab2.server.services

import com.lab2.server.dto.*

interface ProfileService {
    fun getAll(): List<ProfileDTO>
    fun getProfileByEmail(email:String): ProfileDTO?
    fun getTicketsByEmail(email: String): MutableList<TicketDTO>
    fun insertProfile(profile: CreateOrEditProfileDTO, address: CreateOrChangeProfileAddressDTO)
    fun editProfileInfo(oldProfile: ProfileDTO, newProfile: CreateOrEditProfileDTO)

    fun getAddressByEmail(email: String): AddressDTO

    fun editAddressByProfile(profileD: ProfileDTO, newAddress: CreateOrChangeProfileAddressDTO)
}