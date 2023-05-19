package com.lab2.server.data

import com.lab2.server.dto.ManagerDTO
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name="managers")
class Manager (

    var name: String,

    var surname: String

): EntityBase<Long>()
{
    /*
    @OneToMany(mappedBy = "manager")
    var changedStatuses = mutableListOf<TicketStatus>()
    */
}

fun ManagerDTO.toManager(): Manager {
    val manager = Manager(this.name, this.surname)
    manager.id = this.id
    return manager
}
