package com.lab2.server.dto

data class StatsDTO(
    val expertEmail: String,
    val ticketInProgress: Long,
    val totalAssignedEver: Long,
    val totalClosed: Long,
    val totalTimeToSolveTickets: Long,
    val closedPerDay: List<Chart1Data>,
    val closedPerExpertise: List<Chart2Data>
)


data class Chart1Data(
    val data: String,
    val nticketClosed: Long
)

data class Chart2Data(
    val expertise: String,
    val nticketClosed: Long
)