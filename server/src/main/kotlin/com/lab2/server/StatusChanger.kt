package com.lab2.server

import jakarta.persistence.MappedSuperclass

interface StatusChanger {
    fun changeStatus()
}