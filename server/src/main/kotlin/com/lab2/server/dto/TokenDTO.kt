package com.lab2.server.dto

data class TokenDTO (
    val access_token: String,
    val expires_in: Int,
    val refresh_expires_in: Int,
    val refresh_token: String
)