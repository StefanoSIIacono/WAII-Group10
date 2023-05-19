package com.lab2.server.serviceImpl

import com.lab2.server.data.Address
import com.lab2.server.data.Profile
import com.lab2.server.dto.*
import com.lab2.server.exceptionsHandler.exceptions.DuplicateProfileException
import com.lab2.server.exceptionsHandler.exceptions.IllegalEmailInAddressException
import com.lab2.server.exceptionsHandler.exceptions.ProfileNotFoundException
import com.lab2.server.repositories.ProfileRepository
import com.lab2.server.services.ProfileService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProfileServiceImpl(private val profileRepository: ProfileRepository): ProfileService {
    override fun getAll(): List<ProfileDTO> {
        return profileRepository.findAll().map { it.toDTO() }
    }

    override fun getProfileByEmail(email: String): ProfileDTO? {
        return profileRepository.findByIdOrNull(email)?.toDTO()
    }

    override fun getTicketsByEmail(email: String): MutableList<TicketDTO> {
        return profileRepository.findByIdOrNull(email)!!.tickets.map { it.toDTO() }.toMutableList()
    }

    override fun insertProfile(profile: ProfileDTO){
        if (profileRepository.existsById(profile.email))
            throw DuplicateProfileException("Profile exists!")

        val newProfile = Profile(profile.email, profile.name, profile.surname, null)
        val newAddress = Address(
                profile.address!!.city,
                profile.address.country,
                profile.address.zipCode,
                profile.address.street,
                profile.address.houseNumber,
                newProfile,
                newProfile.email)

        newProfile.addAddress(newAddress)

        // The save is in cascade for the address
        profileRepository.save(newProfile)
    }

    override fun editProfileInfo(email: String, newProfile: ChangeProfileInfoDTO){

        val oldProfile = profileRepository.findByIdOrNull(email)
            ?: throw IllegalEmailInAddressException("The email doesn't exist")

        oldProfile.name = newProfile.name
        oldProfile.surname = newProfile.surname

        profileRepository.save(oldProfile)
    }

    override fun editAddressByProfile(email: String, newAddress: GetAddressDTO) {

        val profile = profileRepository.findByIdOrNull(email)
            ?: throw ProfileNotFoundException("Profile not found")

        profile.addAddress(Address(newAddress.city,
                        newAddress.country,
                        newAddress.zipCode,
                        newAddress.street,
                        newAddress.houseNumber,
                        profile,
                        profile.email))

        profileRepository.save(profile)
    }
    // FOR OTHER SERVICES ONLY - DO NOT EXPOSE TO THE CLIENT, SINCE IT RETURNS PROFILE ENTITIES
    override fun provideProfileByEmail(email: String): Profile? {
        return profileRepository.findByIdOrNull(email)
    }
}
