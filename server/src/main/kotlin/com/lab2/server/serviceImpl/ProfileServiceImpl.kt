package com.lab2.server.serviceImpl

import com.lab2.server.data.Profile
import com.lab2.server.data.Roles
import com.lab2.server.data.toAddress
import com.lab2.server.dto.*
import com.lab2.server.exceptionsHandler.exceptions.DuplicateProfileException
import com.lab2.server.exceptionsHandler.exceptions.ProfileNotFoundException
import com.lab2.server.repositories.ProfileRepository
import com.lab2.server.services.ProfileService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import javax.ws.rs.NotAuthorizedException

@Service
class ProfileServiceImpl(private val profileRepository: ProfileRepository) :
    ProfileService {
    override fun getAllPaginated(page: Int, offset: Int): PagedDTO<ProfileDTO> {
        val pageResult = profileRepository.findAll(PageRequest.of(page, offset, Sort.by("name")))
        val meta = PagedMetadata(pageResult.number, pageResult.totalPages, pageResult.numberOfElements)

        return PagedDTO(meta, pageResult.toList().map { it.toDTO() })
    }

    override fun getProfileByEmail(email: String): ProfileDTO {
        return profileRepository.findByIdOrNull(email)?.toDTO() ?: throw ProfileNotFoundException("Profile not found")
    }

    override fun getTicketsByEmailPaginated(
        email: String,
        page: Int,
        offset: Int,
        user: JwtAuthenticationToken
    ): PagedDTO<TicketDTO> {
        val profile = profileRepository.findByIdOrNull(email)

        if (profile === null) {
            throw ProfileNotFoundException("Profile not found")
        }

        val userRole = Roles.values()
            .firstOrNull { sc -> user.authorities.map { it.authority }.contains(sc.name) }
        if (email != user.name && userRole != Roles.MANAGER) {
            throw NotAuthorizedException("Not allowed")
        }

        val totalSize = profile.tickets.size
        var totalPages = totalSize / offset
        if (totalSize % offset != 0) {
            totalPages += 1
        }
        val meta = PagedMetadata(page, totalPages, totalSize)

        return PagedDTO(meta, profile.tickets.toList().subList(page * offset, (page + 1) * offset).map { it.toDTO() })
    }

    override fun insertProfile(profile: ProfileDTO) {
        if (profileRepository.existsById(profile.email)) {
            throw DuplicateProfileException("Profile exists!")
        }

        val profileEntity = Profile(profile.email, profile.name, profile.surname, profile.address.toAddress())

        profileRepository.save(profileEntity)
    }

    override fun editProfileInfo(user: JwtAuthenticationToken, newProfile: ChangeProfileInfoDTO) {

        val oldProfile = profileRepository.findByIdOrNull(user.name)
            ?: throw ProfileNotFoundException("The email doesn't exist")

        oldProfile.name = newProfile.name ?: oldProfile.name
        oldProfile.surname = newProfile.surname ?: oldProfile.surname
        if (newProfile.address !== null) {
            oldProfile.editAddress(newProfile.address.toAddress())
        }

        profileRepository.save(oldProfile)
    }

    // FOR OTHER SERVICES ONLY - DO NOT EXPOSE TO THE CLIENT, SINCE IT RETURNS PROFILE ENTITIES
    override fun unsafeProfileByEmail(email: String): Profile? {
        return profileRepository.findByIdOrNull(email)
    }
}
