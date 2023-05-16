package com.lab2.server.services

import com.lab2.server.dto.AddressDTO
import org.springframework.stereotype.Service

@Service
interface AddressService {

    fun getAll (): List<AddressDTO>

    fun getAddressById(addressId: Long): AddressDTO?
}