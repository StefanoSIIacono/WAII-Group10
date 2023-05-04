package com.lab2.server.ticketing

import jakarta.persistence.*

@Entity
@Table(name="managers")
class Manager (
    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val managerId: Long,
    val name: String,
    val surname: String,
)
{
}