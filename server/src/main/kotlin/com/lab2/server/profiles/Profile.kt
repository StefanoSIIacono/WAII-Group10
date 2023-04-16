package com.lab2.server.profiles

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "profiles")
class Profile (
    @Id
    var email: String,
    var name: String,
    var surname: String
)

fun ProfileDTO.toProfile(): Profile {
    return Profile(email, name, surname)
}
// FIX: usare get e set per i relativi scopi invece di accere direttamente alle proprietà
/*fun Profile.setEmail(email: String) {
    this.email = email
}

fun Profile.setPassword(password: String) {
    this.password = password
}*/