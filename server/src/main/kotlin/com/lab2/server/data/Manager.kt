package com.lab2.server.data

import jakarta.persistence.*

@Entity
@Table(name="managers")
class Manager (
    var name: String,
    var surname: String
): EntityBase<Long>()
