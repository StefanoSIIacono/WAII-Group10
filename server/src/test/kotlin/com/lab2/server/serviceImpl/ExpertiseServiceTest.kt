package com.lab2.server.serviceImpl

import com.lab2.server.data.Expertise
import com.lab2.server.dto.toDTO
import com.lab2.server.exceptionsHandler.exceptions.DuplicatedExpertiseException
import com.lab2.server.repositories.ExpertiseRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ExpertiseServiceTest {
    private val repository = mockk<ExpertiseRepository>()

    @Test
    fun getAllTest() {
        val expertiseList = mutableListOf(
            Expertise("Backend"),
            Expertise("Frontend")
        )
        every { repository.findAll() } returns expertiseList

        val service = ExpertiseServiceImpl(repository)
        // when
        val result = service.getAll()

        // then
        verify(exactly = 1) { repository.findAll() }
        assertEquals(expertiseList.map { it.toDTO() }, result)
    }

    @Test
    fun getExpertiseByFieldTest() {
        // given
        val expertise = Expertise("Backend")
        every { repository.findByField("Backend") } returns expertise

        val service = ExpertiseServiceImpl(repository)
        // when
        val result = service.getExpertise("Backend")

        // then
        verify(exactly = 1) { repository.findByField("Backend") }
        assertEquals(expertise.toDTO(), result)
    }

    @Test
    fun getNotExistingExpertiseByFieldTest() {
        // given
        every { repository.findByField("Backend") } returns null

        val service = ExpertiseServiceImpl(repository)
        // when
        val result = service.getExpertise("Backend")

        // then
        verify(exactly = 1) { repository.findByField("Backend") }
        assertEquals(null, result)
    }

    @Test
    fun createExpertiseTest() {
        // given
        val expertise = Expertise("Backend")
        every { repository.findByField("Backend") } returns null
        every { repository.save(any()) } returns expertise

        val service = ExpertiseServiceImpl(repository)
        // when
        service.createExpertise("Backend")

        // then
        verify(exactly = 1) { repository.findByField("Backend") }
        verify(exactly = 1) { repository.save(any()) }
    }


    @Test
    fun createDuplicateExpertiseTest() {
        // given
        val expertise = Expertise("Backend")
        every { repository.findByField("Backend") } returns expertise

        val service = ExpertiseServiceImpl(repository)
        // when-then
        assertThrows<DuplicatedExpertiseException> {
            service.createExpertise("Backend")
        }

        // then
        verify(exactly = 0) { repository.save(expertise) }
    }

    @Test
    fun deleteExpertiseTest() {
        // given
        val expertise = Expertise("Backend")
        every { repository.findByField("Backend") } returns expertise
        every { repository.delete(expertise) } returns Unit

        val service = ExpertiseServiceImpl(repository)
        // when
        service.deleteExpertise("Backend")

        // then
        verify(exactly = 1) { repository.findByField("Backend") }
        verify(exactly = 1) { repository.delete(expertise) }
    }
}