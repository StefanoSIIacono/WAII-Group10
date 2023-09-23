package com.lab2.server.unit

import com.lab2.server.data.Address
import com.lab2.server.data.Profile
import com.lab2.server.dto.*
import com.lab2.server.exceptionsHandler.exceptions.DuplicateProfileException
import com.lab2.server.exceptionsHandler.exceptions.ProfileNotFoundException
import com.lab2.server.repositories.ProfileRepository
import com.lab2.server.serviceImpl.ProfileServiceImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull

class ProfileServiceTest {
    private val repository = mockk<ProfileRepository>()

    @Test
    fun getAllTest() {
        // given
        val profilePage = PageImpl(
            listOf(
                Profile("mariorossi@gmail.com", "Mario", "Rossi", Address("a", "b", "c", "d")),
                Profile("luigiverdi@gmail.com", "Luigi", "Verdi", Address("a", "b", "c", "d"))
            ), PageRequest.of(0, 100, Sort.by("name")), 2
        )
        every { repository.findAll(PageRequest.of(0, 100, Sort.by("name"))) } returns profilePage

        val service = ProfileServiceImpl(repository)
        // when
        val result = service.getAllPaginated(0, 100)

        // then
        verify(exactly = 1) { repository.findAll(PageRequest.of(0, 100, Sort.by("name"))) }
        assertEquals(
            PagedDTO(
                PagedMetadata(
                    profilePage.number + 1,
                    profilePage.totalPages,
                    profilePage.numberOfElements
                ), profilePage.content.map { it.toDTO() }), result
        )
    }

    @Test
    fun getProfileByEmailTest() {
        // given
        val profile = Profile("mariorossi@gmail.com", "mario", "ross", Address("a", "b", "c", "d"))
        every { repository.findByIdOrNull("mariorossi@gmail.com") } returns profile

        val service = ProfileServiceImpl(repository)
        // when
        val result = service.getProfileByEmail("mariorossi@gmail.com")

        // then
        verify(exactly = 1) { repository.findByIdOrNull("mariorossi@gmail.com") }
        assertEquals(profile.toDTO(), result)
    }

    @Test
    fun getNotExistentProfileByEmailTest() {
        // given
        every { repository.findByIdOrNull("mariorossi@gmail.com") } returns null

        val service = ProfileServiceImpl(repository)
        // when
        try {
            service.getProfileByEmail("mariorossi@gmail.com")
        } catch (e: ProfileNotFoundException) {
            assertEquals("Profile not found", e.message)
        }

        // then
        verify(exactly = 1) { repository.findByIdOrNull("mariorossi@gmail.com") }
    }

    @Test
    fun insertProfileTest() {
        // given
        val createAddress = AddressDTO("mariorossi@gmail.com", "c", "c", "z")
        val createProfile = ProfileDTO("mariorossi@gmail.com", "mario", "rossi", createAddress)
        val profile = Profile("mariorossi@gmail.com", "mario", "rossi", Address("a", "b", "c", "d"))
        val address = Address(
            createAddress.address,
            createAddress.zipCode,
            createAddress.city,
            createAddress.country
        )
        profile.editAddress(address)
        every { repository.existsById("mariorossi@gmail.com") } returns false
        every { repository.save(any()) } returns profile


        val service = ProfileServiceImpl(repository)
        // when
        service.insertProfile(createProfile)

        // then
        verify(exactly = 1) { repository.existsById("mariorossi@gmail.com") }
        verify(exactly = 1) { repository.save(any()) }
    }

    @Test
    fun insertExistingProfileTest() {
        // given
        val createAddress = AddressDTO("mariorossi@gmail.com", "c", "c", "z")
        val createProfile = ProfileDTO("mariorossi@gmail.com", "mario", "rossi", createAddress)
        val profile = Profile("mariorossi@gmail.com", "mario", "rossi", Address("a", "b", "c", "d"))
        val address = Address(
            createAddress.address,
            createAddress.zipCode,
            createAddress.city,
            createAddress.country
        )

        profile.editAddress(address)
        every { repository.existsById("mariorossi@gmail.com") } returns true
        every { repository.save(any()) } returns profile

        val service = ProfileServiceImpl(repository)
        // when
        var exceptionThrown = false
        try {
            service.insertProfile(createProfile)
        } catch (e: DuplicateProfileException) {
            exceptionThrown = true
        }

        // then

        verify(exactly = 1) { repository.existsById("mariorossi@gmail.com") }
        verify(exactly = 0) { repository.save(any()) }
        assertEquals(exceptionThrown, true)
    }
}