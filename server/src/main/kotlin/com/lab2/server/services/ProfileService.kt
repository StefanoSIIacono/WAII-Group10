package com.lab2.server.services

import com.lab2.server.dto.*

interface ProfileService {
    fun getAll(): List<ProfileDTO>
    fun getProfileByEmail(email:String): ProfileDTO?
    fun getTicketsByEmail(email: String): MutableList<TicketDTO>
    fun insertProfile(profile: ProfileDTO)//, address: CreateOrChangeProfileAddressDTO)
    fun editProfileInfo(oldProfile: ProfileDTO, newProfile: ProfileDTO)

    //fun getAddressByEmail(email: String): AddressDTO

    fun editAddressByProfile(profileD: ProfileDTO, newAddress: AddressDTO)
}