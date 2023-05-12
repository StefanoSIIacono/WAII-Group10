package com.lab2.server.data

import java.io.Serializable
import jakarta.persistence.*
import org.springframework.data.util.ProxyUtils

@MappedSuperclass
abstract class EntityBase <T: Serializable> {
    companion object {  private const val serialVersionUID = -43869754L   }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "generator")
    @SequenceGenerator(name="generator",
        sequenceName = "sequence_1",
        initialValue = 1,
        allocationSize = 1)
    @Column(updatable = false, nullable = false)
    var id:T?  = null

    //fun getId(): T? = id

    override fun toString(): String {
        return "@Entity ${this.javaClass.name}(id=$id)"    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other === this) return true
        if (javaClass != ProxyUtils.getUserClass(other)) return false
        other as EntityBase<*>
        return if (this.id==null) false
        else this.id == other.id
    }

    override fun hashCode(): Int {
        return 31
    }
}

