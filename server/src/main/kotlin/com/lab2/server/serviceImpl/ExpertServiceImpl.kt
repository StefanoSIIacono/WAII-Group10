package com.lab2.server.serviceImpl

import com.lab2.server.data.Expert
import com.lab2.server.data.Expertise
import com.lab2.server.data.toExpertise
import com.lab2.server.dto.ExpertDTO
import com.lab2.server.dto.PagedDTO
import com.lab2.server.dto.PagedMetadata
import com.lab2.server.dto.toDTO
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
        val meta = PagedMetadata(pageResult.number, pageResult.totalPages, pageResult.numberOfElements)
        return PagedDTO(meta, pageResult.toList().map { it.toDTO() })
    }

    override fun getExpertByEmail(email: String): ExpertDTO {
        return expertRepository.findByIdOrNull(email)?.toDTO() ?: throw ExpertNotFoundException("Expert not found")
    }

    override fun insertExpert(expert: ExpertDTO) {
        val expertEntity = Expert(
            expert.email,
            expert.name,
            expert.surname,
            expert.expertises.map { Expertise(it.field) }.toMutableSet()
        )
        expertRepository.save(expertEntity)
    }

    override fun addExpertiseToExpert(expertEmail: String, expertise: String) {
        val expertiseObject = expertiseService.getExpertise(expertise)
        val expert = expertRepository.findByIdOrNull(expertEmail) ?: throw ExpertNotFoundException("Expert not found")
        expert.addExpertise(expertiseObject.toExpertise())
        expertRepository.save(expert)
    }

    override fun removeExpertiseFromExpert(expertEmail: String, expertise: String) {
        val expertiseObject = expertiseService.getExpertise(expertise)
        val expert = expertRepository.findByIdOrNull(expertEmail) ?: throw ExpertNotFoundException("Expert not found")
        expert.removeExpertise(expertiseObject.toExpertise())
        expertRepository.save(expert)
    }

    override fun unsafeGetExpertByEmail(email: String): Expert? {
        return expertRepository.findByIdOrNull(email)
    }
}