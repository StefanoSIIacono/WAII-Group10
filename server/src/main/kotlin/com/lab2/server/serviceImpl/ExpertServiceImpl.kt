package com.lab2.server.serviceImpl

import com.lab2.server.data.Expert
import com.lab2.server.data.toExpertise
import com.lab2.server.dto.ExpertDTO
import com.lab2.server.dto.PagedDTO
import com.lab2.server.dto.PagedMetadata
import com.lab2.server.dto.toDTO
import com.lab2.server.exceptionsHandler.exceptions.ExpertNotFoundException
import com.lab2.server.exceptionsHandler.exceptions.ExpertiseNotFoundException
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

    override fun getExpertByEmail(email: String): ExpertDTO {
        return expertRepository.findByIdOrNull(email)?.toDTO() ?: throw ExpertNotFoundException("Expert not found")
    }

    override fun insertExpert(expert: ExpertDTO) {
        val expertEntity = Expert(
            expert.email,
            expert.name,
            expert.surname
        )
        expert.expertises.forEach { expertEntity.addExpertise(expertiseService.getExpertise(it.field).toExpertise()) }
        expertRepository.save(expertEntity)
    }

    override fun addExpertiseToExpert(expertEmail: String, expertise: String) {
        val expertiseObject =
            expertiseService.unsafeGetExpertise(expertise) ?: throw ExpertiseNotFoundException("Expertise not found")
        val expert = expertRepository.findByIdOrNull(expertEmail) ?: throw ExpertNotFoundException("Expert not found")
        expert.addExpertise(expertiseObject)
        expertRepository.save(expert)
    }

    override fun removeExpertiseFromExpert(expertEmail: String, expertise: String) {
        val expertiseObject =
            expertiseService.unsafeGetExpertise(expertise) ?: throw ExpertiseNotFoundException("Expertise not found")
        val expert = expertRepository.findByIdOrNull(expertEmail) ?: throw ExpertNotFoundException("Expert not found")
        expert.removeExpertise(expertiseObject)
        expertRepository.save(expert)
    }

    override fun unsafeGetExpertByEmail(email: String): Expert? {
        return expertRepository.findByIdOrNull(email)
    }
}