package com.lab2.server.dto

data class UnreadMessagesDTO(
    val ticket: Long,
    val lastReadIndex: Int,
    val unreadMessagesNumber: Int,
)