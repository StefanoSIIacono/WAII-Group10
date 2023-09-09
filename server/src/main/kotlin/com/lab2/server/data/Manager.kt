package com.lab2.server.data

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "managers")
class Manager(

    @Id
    @Column(updatable = false, nullable = false)
    val email: String,

    var name: String,
    var surname: String
)