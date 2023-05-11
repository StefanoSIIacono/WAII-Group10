package com.lab2.server.data

import com.lab2.server.dto.ProfileDTO
import jakarta.persistence.*

@Entity
@Table(name = "profiles")
class Profile (
    @Id
    @Column(updatable = false, nullable = false)
    var email: String,
    var name: String,
    var surname: String
)
{
    @OneToMany( mappedBy = "profile")
    val tickets = mutableListOf<Ticket>()

    fun addTicket(t: Ticket){
        t.profile = this
        this.tickets.add(t)
    }

    /*fun getEmail(): String{
        return this.email
    }*/
}

fun ProfileDTO.toProfile(): Profile {
    return Profile(email, name, surname)
}


