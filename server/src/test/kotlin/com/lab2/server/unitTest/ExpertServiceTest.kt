package com.lab2.server.unitTest

import com.lab2.server.data.*
import com.lab2.server.dto.ExpertDTO
import com.lab2.server.dto.ExpertiseDTO
import com.lab2.server.dto.toDTO
import com.lab2.server.exceptionsHandler.exceptions.ExpertNotFoundException
import com.lab2.server.repositories.ExpertRepository
import com.lab2.server.serviceImpl.ExpertServiceImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull

class ExpertServiceTest {
    private val repository = mockk<ExpertRepository>()

    @Test
    fun getAllTest() {
        val expertList = mutableListOf(
            Expert("John", "Doe"),
            Expert("Jane", "Smith")
        )
        every { repository.findAll() } returns expertList

        val service = ExpertServiceImpl(repository)
        // when
        val result = service.getAll()

        // then
        verify(exactly = 1) { repository.findAll() }
        assertEquals(expertList.map { it.toDTO() }.toMutableSet(), result)
    }

    @Test
    fun getExpertByIdTest() {
        // given
        val expertId = 1L
        val expert = Expert("John", "Doe")
        every { repository.findByIdOrNull(expertId) } returns expert

        val service = ExpertServiceImpl(repository)
        // when
        val result = service.getExpertById(expertId)

        // then
        verify(exactly = 1) { repository.findByIdOrNull(expertId) }
        assertEquals(expert.toDTO(), result)
    }

    @Test
    fun getExpertByIdNotFoundTest() {
        // given
        val expertId = 1L
        every { repository.findByIdOrNull(expertId) } returns null

        val service = ExpertServiceImpl(repository)
        // when/then
        try {
            service.getExpertById(expertId)
        } catch (e: ExpertNotFoundException) {
            assertEquals("Expert not found", e.message)
        }

        // then
        verify(exactly = 1) { repository.findByIdOrNull(expertId) }
    }

    @Test
    fun getExpertisesByExpertTest() {
        // given
        val expertId = 1L
        val expert = Expert("John", "Doe")
        val expertiseList = mutableListOf(
            Expertise("Backend"),
            Expertise("Frontend")
        )
        expert.expertises.addAll(expertiseList)
        every { repository.findByIdOrNull(expertId) } returns expert

        val service = ExpertServiceImpl(repository)
        // when
        val result = service.getExpertisesByExpert(expertId)

        // then
        verify(exactly = 1) { repository.findByIdOrNull(expertId) }
        assertEquals(expertiseList.map { it.toDTO() }.toMutableSet(), result)
    }

    @Test
    fun getExpertisesByExpertNotFoundTest() {
        // given
        val expertId = 1L
        every { repository.findByIdOrNull(expertId) } returns null

        val service = ExpertServiceImpl(repository)
        // when/then
        try {
            service.getExpertisesByExpert(expertId)
        } catch (e: ExpertNotFoundException) {
            assertEquals("Expert not found", e.message)
        }

        // then
        verify(exactly = 1) { repository.findByIdOrNull(expertId) }
    }

    @Test
    fun insertExpertTest() {
        // given
        val expertName = "John"
        val expertSurname = "Doe"
        val expert = Expert(expertName, expertSurname)
        every { repository.findByIdOrNull(1L) } returns null
        every { repository.save(any()) } returns expert

        val service = ExpertServiceImpl(repository)
        // when
        service.insertExpert(expertName, expertSurname)

        // then
        verify(exactly = 1) { repository.save(any()) }
    }


    @Test
    fun addExpertiseToExpertTest() {
        // given
        val expertD = ExpertDTO(1L, "John", "Doe")
        val expertise = ExpertiseDTO(1L, "Backend")
        val expert = expertD.toExpert()
        //val expertiseEntity = expertise.toExpertise()

        every { repository.findByIdOrNull(expertD.id!!) } returns expert
        every { repository.save(any()) } returns expert

        val service = ExpertServiceImpl(repository)
        // when
        service.addExpertiseToExpert(expertD, expertise)

        // then
        verify(exactly = 1) { repository.findByIdOrNull(expertD.id!!) }
        verify(exactly = 1) { repository.save(expert) }
    }

    @Test
    fun addTicketToExpertTest() {
        // given
        val expert = Expert("John", "Doe")
        val product = Product("1", "Shirt", "Tommy Hilfiger")
        val profile = Profile("test@test1.com", "testName", "testSurname",
                null)
        val address = Address("c", "c", "z", "s", "h", profile)
        profile.addAddress(address)
        val ticket = Ticket("Ticket", "ticket problem", Priority.TOASSIGN, profile, null, product)

        every { repository.save(any()) } returns expert

        val service = ExpertServiceImpl(repository)
        // when
        service.addTicketToExpert(expert, ticket)

        // then
        assertEquals(1, expert.inProgressTickets.size)
        assertTrue(expert.inProgressTickets.contains(ticket))
        verify(exactly = 1) { repository.save(expert) }
    }
}