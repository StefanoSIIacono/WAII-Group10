package com.lab2.server.data

import jakarta.persistence.*

@Entity
@Table(name = "profiles")
class Profile(
    @Id
    @Column(updatable = false, nullable = false)
    var email: String,
    var name: String,
    var surname: String,

    @OneToOne(mappedBy = "profile", cascade = [CascadeType.ALL], orphanRemoval = true)
    var address: Address,
) {
    @OneToMany(mappedBy = "profile")
    val tickets: MutableList<Ticket> = mutableListOf()
    fun editAddress(a: Address) {
        this.address = a
        a.profile = this
    }
}




