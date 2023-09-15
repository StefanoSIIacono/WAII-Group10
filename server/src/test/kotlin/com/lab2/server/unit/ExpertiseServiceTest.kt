package com.lab2.server.unit

import com.lab2.server.data.Expertise
import com.lab2.server.dto.PagedDTO
import com.lab2.server.dto.PagedMetadata
import com.lab2.server.dto.toDTO
import com.lab2.server.exceptionsHandler.exceptions.DuplicatedExpertiseException
import com.lab2.server.exceptionsHandler.exceptions.ExpertiseNotFoundException
import com.lab2.server.repositories.ExpertiseRepository
import com.lab2.server.serviceImpl.ExpertiseServiceImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

class ExpertiseServiceTest {
    private val repository = mockk<ExpertiseRepository>()

    @Test
    fun getAllTest() {
        val expertisePage = PageImpl(
            listOf(
                Expertise("Backend"),
                Expertise("Frontend")
            ), PageRequest.of(0, 100, Sort.by("field")), 2
        )
        every { repository.findAll(PageRequest.of(0, 100, Sort.by("field"))) } returns expertisePage

        val service = ExpertiseServiceImpl(repository)
        // when
        val result = service.getAllPaginated(0, 100)

        // then
        verify(exactly = 1) { repository.findAll(PageRequest.of(0, 100, Sort.by("field"))) }
        assertEquals(
            PagedDTO(
                PagedMetadata(
                    expertisePage.number + 1,
                    expertisePage.totalPages,
                    expertisePage.numberOfElements
                ), expertisePage.content.map { it.toDTO() }), result
        )
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
        try {
            service.getExpertise("Backend")
        } catch (e: ExpertiseNotFoundException) {
            assertEquals("Expertise doesn't exists!", e.message)
        }

        // then
        verify(exactly = 1) { repository.findByField("Backend") }
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