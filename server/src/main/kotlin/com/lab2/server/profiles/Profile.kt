package com.lab2.server.profiles

import com.lab2.server.ticketing.Ticket
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "profiles")
class Profile (
    @Id
    var email: String,
    var name: String,
    var surname: String
)
{
    @OneToMany(mappedBy = "profile")
    var tickets = mutableListOf<Ticket>()

    fun addTicket(t: Ticket){
        t.profile = this;
        this.tickets.add(t);
    }
}

fun ProfileDTO.toProfile(): Profile {
    return Profile(email, name, surname)
}
