package com.lab2.server.data

import com.lab2.server.dto.ProfileDTO
import jakarta.persistence.*

@Entity
@Table(name = "profiles")
class Profile(
    @Id
    @Column(updatable = false, nullable = false)
    var email: String,
    var name: String,
    var surname: String,

    @OneToOne(mappedBy = "profile", cascade = [CascadeType.ALL])
    var address: Address,
) {
    @OneToMany(mappedBy = "profile")
    val tickets: MutableList<Ticket> = mutableListOf()
    fun editAddress(a: Address) {
        this.address = a
        a.profile = this
    }
}

fun ProfileDTO.toProfile(): Profile {
    return Profile(this.email, this.name, this.surname, this.address.toAddress())
}




