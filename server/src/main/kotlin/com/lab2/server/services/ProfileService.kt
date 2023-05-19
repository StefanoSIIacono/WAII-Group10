package com.lab2.server.services

import com.lab2.server.data.Profile
import com.lab2.server.dto.ChangeProfileInfoDTO
import com.lab2.server.dto.GetAddressDTO
import com.lab2.server.dto.ProfileDTO
import com.lab2.server.dto.TicketDTO

interface ProfileService {
    fun getAll(): List<ProfileDTO>
    fun getProfileByEmail(email:String): ProfileDTO?
    fun getTicketsByEmail(email: String): MutableList<TicketDTO>
    fun insertProfile(profile: ProfileDTO)//, address: CreateOrChangeProfileAddressDTO)
    fun editProfileInfo(email: String, newProfile: ChangeProfileInfoDTO)

    //fun getAddressByEmail(email: String): AddressDTO

    fun editAddressByProfile(email: String, newAddress: GetAddressDTO)

    // Service getProfileByEmail implementation
    fun provideProfileByEmail(email: String): Profile?
}