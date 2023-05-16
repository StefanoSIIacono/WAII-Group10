package com.lab2.server.serviceImpl

import com.lab2.server.dto.AddressDTO
import com.lab2.server.dto.toDTO
import com.lab2.server.repositories.AddressRepository
import com.lab2.server.services.AddressService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AddressServiceImpl(private val addressRepository: AddressRepository): AddressService {
    override fun getAll(): List<AddressDTO> {
        return addressRepository.findAll().map { it.toDTO() }
    }

    override fun getAddressById(addressId: Long): AddressDTO? {
        return addressRepository.findByIdOrNull(addressId)?.toDTO()
    }
}