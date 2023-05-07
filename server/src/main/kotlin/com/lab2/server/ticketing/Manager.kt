package com.lab2.server.ticketing

import com.lab2.server.EntityBase
import jakarta.persistence.*

@Entity
@Table(name="managers")
class Manager (
    var name: String,
    var surname: String
): EntityBase<Long>()
{
}