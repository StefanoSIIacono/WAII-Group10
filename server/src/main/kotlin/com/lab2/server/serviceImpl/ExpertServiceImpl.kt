package com.lab2.server.serviceImpl

import com.lab2.server.data.Expert
import com.lab2.server.data.Expertise
import com.lab2.server.dto.*
import com.lab2.server.exceptionsHandler.exceptions.ExpertNotFoundException
import com.lab2.server.repositories.ExpertRepository
import com.lab2.server.services.ExpertService
import com.lab2.server.services.ExpertiseService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ExpertServiceImpl(
    private val expertRepository: ExpertRepository,
    private val expertiseService: ExpertiseService
) : ExpertService {

    override fun getAllPaginated(page: Int, offset: Int): PagedDTO<ExpertDTO> {
        val pageResult = expertRepository.findAll(PageRequest.of(page, offset, Sort.by("name")))
        val meta = PagedMetadata(pageResult.number + 1, pageResult.totalPages, pageResult.numberOfElements)
        return PagedDTO(meta, pageResult.content.map { it.toDTO() })
    }

    override fun searchByEmailAndExpertisePaginated(
        email: String,
        expertise: String?,
        page: Int,
        offset: Int
    ): PagedDTO<ExpertDTO> {
        val pageResult = if (expertise === null) expertRepository.findByEmailContaining(
            email,
            PageRequest.of(page, offset, Sort.by("name"))
        ) else expertRepository.findByEmailContainingAndExpertisesField(
            email,
            expertise,
            PageRequest.of(page, offset, Sort.by("name"))
        )
        val meta = PagedMetadata(pageResult.number + 1, pageResult.totalPages, pageResult.numberOfElements)
        return PagedDTO(meta, pageResult.content.map { it.toDTO() })
    }

    override fun getExpertByEmail(email: String): ExpertDTO {
        return expertRepository.findByIdOrNull(email)?.toDTO() ?: throw ExpertNotFoundException("Expert not found")
    }

    override fun insertExpert(expert: ExpertDTO) {
        val expertEntity = Expert(
            expert.email,
            expert.name,
            expert.surname
        )
        expert.expertises.forEach { expertEntity.addExpertise(Expertise(it.field)) }
        expertRepository.save(expertEntity)
    }

    override fun addExpertiseToExpert(expertEmail: String, expertise: String) {
        val expert = expertRepository.findByIdOrNull(expertEmail) ?: throw ExpertNotFoundException("Expert not found")
        expert.addExpertise(Expertise(expertise))
        expertRepository.save(expert)
    }

    override fun removeExpertiseFromExpert(expertEmail: String, expertise: String) {
        val expert = expertRepository.findByIdOrNull(expertEmail) ?: throw ExpertNotFoundException("Expert not found")
        expert.removeExpertise(Expertise(expertise))
        expertRepository.save(expert)
    }

    override fun getExpertStats(email: String): StatsDTO {
        getExpertByEmail(email)
        val totalInProgress = expertRepository.countCurrentlyAssignedToExpert(email)
        val totalAssignedEver = expertRepository.countAllEverAssignedToExpert(email)
        val totalClosed = expertRepository.totalClosed(email)
        val totalTimeToSolveTickets = expertRepository.totalTimeToSolveTickets(email)
        val closedPerExpertise = expertRepository.closedPerExpertise(email)
        val closedPerDay = expertRepository.closedPerDays(email)

        return StatsDTO(
            email,
            totalInProgress ?: 0,
            totalAssignedEver ?: 0,
            totalClosed ?: 0,
            totalTimeToSolveTickets ?: 0,
            closedPerDay,
            closedPerExpertise
        )
    }

    override fun unsafeGetExpertByEmail(email: String): Expert? {
        return expertRepository.findByIdOrNull(email)
    }
}