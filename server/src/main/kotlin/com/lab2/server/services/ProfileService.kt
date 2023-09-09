package com.lab2.server.services

import com.lab2.server.data.Profile
import com.lab2.server.dto.ChangeProfileInfoDTO
import com.lab2.server.dto.PagedDTO
import com.lab2.server.dto.ProfileDTO
import com.lab2.server.dto.TicketDTO
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

interface ProfileService {
    fun getAllPaginated(page: Int, offset: Int): PagedDTO<ProfileDTO>
    fun getProfileByEmail(email: String): ProfileDTO
    fun getTicketsByEmail(email: String, user: JwtAuthenticationToken): MutableList<TicketDTO>
    fun insertProfile(profile: ProfileDTO)
    fun editProfileInfo(user: JwtAuthenticationToken, newProfile: ChangeProfileInfoDTO)

    fun unsafeProfileByEmail(email: String): Profile?
}