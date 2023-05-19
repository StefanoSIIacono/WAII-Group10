package com.lab2.server.serviceImpl

import com.lab2.server.repositories.AddressRepository
import com.lab2.server.services.AddressService
import org.springframework.stereotype.Service

@Service
class AddressServiceImpl(private val addressRepository: AddressRepository): AddressService {
 /*   override fun getAll(): List<GetAddressDTO> {
        return addressRepository.findAll().map { it.toDTO() }
    }

    override fun getAddressById(addressId: Long): GetAddressDTOAddressDTO? {
        return addressRepository.findByIdOrNull(addressId)?.toDTO()
    }
  */
}