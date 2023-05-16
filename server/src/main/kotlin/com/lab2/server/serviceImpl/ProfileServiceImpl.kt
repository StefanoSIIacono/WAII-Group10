package com.lab2.server.serviceImpl

import com.lab2.server.data.Address
import com.lab2.server.data.Profile
import com.lab2.server.data.toProfile
import com.lab2.server.dto.*
import com.lab2.server.exceptionsHandler.exceptions.DuplicateProfileException
import com.lab2.server.exceptionsHandler.exceptions.ProfileEmailChangeNotAllowedException
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

    override fun insertProfile(profile: CreateOrEditProfileDTO, address: CreateOrChangeProfileAddressDTO){
        if (profileRepository.existsById(profile.email))
            throw DuplicateProfileException("Profile exists!")

        val newProfile = Profile(profile.email, profile.name, profile.surname, null)
        val newAddress = Address(address.city, address.country, address.zipCode,address.street, address.houseNumber, newProfile)

        newProfile.addAddress(newAddress)

        // The save of the profile saves the address in cascade
        profileRepository.save(newProfile)
    }
/*--------------------------------------------------------------------------------------------------
    I provided two new methods to change the address or the profile info (name, surname)
---------------------------------------------------------------------------------------------------*/
    override fun editProfileInfo(oldProfile: ProfileDTO, newProfile: CreateOrEditProfileDTO){
        if (oldProfile.email != newProfile.email)
            throw ProfileEmailChangeNotAllowedException("Can't change profile email")

        /* check already done in controller in order to pass a DTO to the service
        if (!profileRepository.existsById(profile.email))
            throw ProfileNotFoundException("Profile doesn't exist!")
        */
        val profile = oldProfile.toProfile()
        profile.name = newProfile.name
        profile.surname = newProfile.surname

        profileRepository.save(profile)
    }

//--------------------------------------------------------------------------------------------------------




    override fun getAddressByEmail(email: String): AddressDTO {
        val profile = profileRepository.findByIdOrNull(email) ?:throw ProfileNotFoundException("Profile not found")

        return profile.address!!.toDTO()
    }

    // Function used to CHANGE ADDRESS TO THE ALREADY CREATED PROFILE
    override fun editAddressByProfile(profileD: ProfileDTO, newAddress: CreateOrChangeProfileAddressDTO) {

        //val profile = profileRepository.findByIdOrNull(newAddress.email) ?: throw ProfileNotFoundException("Profile not found")
        val profile = profileD.toProfile()
        profile.addAddress(Address(newAddress.city, newAddress.country, newAddress.zipCode, newAddress.street,
            newAddress.houseNumber, profile))

        profileRepository.save(profile)
    }
}
