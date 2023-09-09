package com.lab2.server.data

import jakarta.persistence.*

@Entity
@Table(name = "tickets")
class Ticket(
    val obj: String,

    @ManyToOne
    var arg: Expertise,

    @Enumerated(value = EnumType.STRING)
    var priority: Priority,

    @ManyToOne
    var profile: Profile,

    @ManyToOne
    var expert: Expert? = null,

    @ManyToOne
    var product: Product,
) : EntityBase<Long>() {
    @OneToMany(mappedBy = "ticket", cascade = [CascadeType.ALL])
    var statusHistory = mutableListOf<TicketStatus>()

    @OneToMany(mappedBy = "ticket", cascade = [CascadeType.ALL])
    var messages = mutableListOf<Message>()

    var lastReadMessageIndexProfile: Int = 0
    var lastReadMessageIndexExpert: Int = 0

    fun changePriority(p: Priority) {
        this.priority = p
    }

    fun removeExpert() {
        this.expert?.inProgressTickets?.filter { it.id === this.id }
        this.expert = null
    }

    fun addStatus(s: TicketStatus, sc: Roles, e: Expert? = null) {
        s.ticket = this
        s.statusChanger = sc
        s.expert = e
        statusHistory.add(s)
    }

    fun addMessageFromExpert(m: Message) {
        m.expert = this.expert
        m.ticket = this
        this.messages.add(m)
    }

    fun addMessageFromProfile(m: Message) {
        m.expert = null
        m.ticket = this
        this.messages.add(m)
    }

    fun updateLastReadExpert(index: Int?) {
        lastReadMessageIndexExpert = index ?: messages.size
    }

    fun updateLastReadProfile(index: Int?) {
        lastReadMessageIndexProfile = index ?: messages.size
    }
}

enum class Priority {
    TOASSIGN,
    LOW,
    MEDIUM,
    HIGH
}
