package com.lab2.server.data

enum class Status {
    IN_PROGRESS,
    OPEN,
    REOPENED,
    RESOLVED,
    CLOSED
}

val validStatusChanges = mapOf(
    Status.OPEN to arrayOf(Status.RESOLVED, Status.CLOSED, Status.IN_PROGRESS),
    Status.RESOLVED to arrayOf(Status.CLOSED, Status.REOPENED),
    Status.CLOSED to arrayOf(Status.REOPENED),
    Status.IN_PROGRESS to arrayOf(Status.OPEN, Status.CLOSED, Status.RESOLVED),
    Status.REOPENED to arrayOf(Status.IN_PROGRESS, Status.CLOSED, Status.RESOLVED),
)