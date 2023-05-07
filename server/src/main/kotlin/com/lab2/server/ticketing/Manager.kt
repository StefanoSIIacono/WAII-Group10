package com.lab2.server.ticketing

import ch.qos.logback.core.status.StatusListener
import com.lab2.server.EntityBase
import com.lab2.server.StatusChanger
import jakarta.persistence.*

@Entity
@Table(name="managers")
class Manager (
    var name: String,
    var surname: String
): EntityBase<Long>(), StatusChanger
{
    override fun changeStatus() {
        //
    }
}