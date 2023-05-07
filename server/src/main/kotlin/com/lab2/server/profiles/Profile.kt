package com.lab2.server.profiles

import com.lab2.server.Chatter
import com.lab2.server.StatusChanger
import com.lab2.server.ticketing.Ticket
import jakarta.persistence.*

@Entity
@Table(name = "profiles")
class Profile (
    @Id
    @Column(updatable = false, nullable = false)
    var email: String,
    var name: String,
    var surname: String
): StatusChanger, Chatter
{
    @OneToMany(mappedBy = "profile")
    val tickets = mutableListOf<Ticket>()

    override fun changeStatus() {
        TODO("Not yet implemented")
    }

    fun addTicket(t: Ticket){
        t.profile = this;
        this.tickets.add(t);
    }

    override fun writeMessage() {
        TODO("Not yet implemented")
    }
}

fun ProfileDTO.toProfile(): Profile {
    return Profile(email, name, surname)
}
