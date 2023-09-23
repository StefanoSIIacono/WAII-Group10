package com.lab2.server.serviceImpl


import com.lab2.server.data.Expertise
import com.lab2.server.dto.*
import com.lab2.server.exceptionsHandler.exceptions.DuplicatedExpertiseException
import com.lab2.server.exceptionsHandler.exceptions.ExpertiseCurrentlyInUseException
import com.lab2.server.exceptionsHandler.exceptions.ExpertiseNotFoundException
import com.lab2.server.repositories.ExpertiseRepository
import com.lab2.server.services.ExpertiseService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import kotlin.math.min

@Service
class ExpertiseServiceImpl(private val expertiseRepository: ExpertiseRepository) : ExpertiseService {

    override fun getExpertsByExpertisePaginated(field: String, page: Int, offset: Int): PagedDTO<ExpertDTO> {
        val exp =
            expertiseRepository.findByField(field) ?: throw ExpertiseNotFoundException("Expertise doesn't exists!")

        val totalSize = exp.experts.size
        var totalPages = totalSize / offset
        if (totalSize % offset != 0) {
            totalPages += 1
        }
        val meta = PagedMetadata(page + 1, totalPages, totalSize)

        return PagedDTO(
            meta,
            exp.experts.toList().subList(min(page * offset, totalSize), min(totalSize, (page + 1) * offset))
                .map { it.toDTO() })
    }

    override fun getAllPaginated(page: Int, offset: Int): PagedDTO<ExpertiseDTO> {
        val pageResult = expertiseRepository.findAll(PageRequest.of(page, offset, Sort.by("field")))
        val meta = PagedMetadata(pageResult.number + 1, pageResult.totalPages, pageResult.numberOfElements)

        return PagedDTO(meta, pageResult.content.map { it.toDTO() })
    }

    override fun searchByFieldPaginated(field: String, page: Int, offset: Int): PagedDTO<ExpertiseDTO> {
        val pageResult =
            expertiseRepository.findByFieldContaining(field, PageRequest.of(page, offset, Sort.by("field")))
        val meta = PagedMetadata(pageResult.number + 1, pageResult.totalPages, pageResult.numberOfElements)

        return PagedDTO(meta, pageResult.content.map { it.toDTO() })
    }

    override fun getExpertise(field: String): ExpertiseDTO {
        return expertiseRepository.findByField(field)?.toDTO()
            ?: throw ExpertiseNotFoundException("Expertise doesn't exists!")
    }

    override fun createExpertise(field: String) {
        if (expertiseRepository.findByField(field) !== null) {
            throw DuplicatedExpertiseException("Expertise exists!")
        }
        expertiseRepository.save(Expertise(field))
    }

    override fun deleteExpertise(expertise: String) {
        val expertiseEntity =
            expertiseRepository.findByField(expertise) ?: throw ExpertiseNotFoundException("Expertise not found")

        if (expertiseEntity.experts.size > 0) {
            throw ExpertiseCurrentlyInUseException("Expertise that has been used can't be deleted")
        }

        expertiseRepository.delete(expertiseEntity)
    }

    override fun unsafeGetExpertise(expertise: String): Expertise? {
        return expertiseRepository.findByField(expertise)
    }
}