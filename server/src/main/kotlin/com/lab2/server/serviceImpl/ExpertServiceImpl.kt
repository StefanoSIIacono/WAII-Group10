package com.lab2.server.serviceImpl

import com.lab2.server.data.Expert
import com.lab2.server.data.Ticket
import com.lab2.server.data.toExpertise
import com.lab2.server.dto.ExpertDTO
import com.lab2.server.dto.ExpertiseDTO
import com.lab2.server.dto.toDTO
import com.lab2.server.exceptionsHandler.exceptions.ExpertNotFoundException
import com.lab2.server.exceptionsHandler.exceptions.ExpertiseNotFoundException
import com.lab2.server.repositories.ExpertRepository
import com.lab2.server.services.ExpertService
import com.lab2.server.services.ExpertiseService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ExpertServiceImpl(private val expertRepository: ExpertRepository, private val expertiseService: ExpertiseService): ExpertService {

    override fun getAll(): MutableSet<ExpertDTO> {
        return expertRepository.findAll().map { it.toDTO() }.toMutableSet()
    }
    override fun getExpertByEmail(email: String): ExpertDTO? {
        return expertRepository.findByIdOrNull(email)?.toDTO() ?: throw ExpertNotFoundException("Expert not found")
    }

    override fun getExpertisesByExpert(email: String): MutableSet<ExpertiseDTO> {
        return expertRepository.findByIdOrNull(email)?.toDTO()?.expertises ?: throw ExpertNotFoundException("Expert not found")
    }

    override fun insertExpert(expert: ExpertDTO, expertises: MutableSet<String>?) {
        expertRepository.save(Expert(expert.email, expert.name, expert.surname))
        expertises?.forEach { println(it); this.addExpertiseToExpert(expert.email, it) }
    }

    override fun addExpertiseToExpert(expertEmail: String, expertise: String) {
        val expertiseObject = expertiseService.getExpertise(expertise)
            ?: throw ExpertiseNotFoundException("Expertise not found")
        val expert = expertRepository.findByIdOrNull(expertEmail) ?: throw ExpertNotFoundException("Expert not found")
        //expert.addExpertiseDTO(expertise)
        expert.addExpertise(expertiseObject.toExpertise())
        expertRepository.save(expert)
    }

    override fun addTicketToExpert(expert: Expert, ticket: Ticket) {

        expert.inProgressTickets.add(ticket)
        expertRepository.save(expert)
    }
}