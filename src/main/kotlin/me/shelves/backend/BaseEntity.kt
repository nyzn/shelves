package me.shelves.backend

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.xml.bind.annotation.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
abstract class BaseEntity : Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    @XmlTransient
    @XmlID
    val id: Long? = null

    @NotNull
    @Column(name = "uuid" , unique =true)
    val uuid: String = UUID.randomUUID().toString()

    @CreatedDate
    @XmlTransient
    val createdAt: Date? = null

    @LastModifiedDate
    @XmlTransient
    val lastModifiedDate: Date? = null


    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other !is BaseEntity)
            return false
        if (id != other.id)
            return false

        return true
    }


    /**
    @CreatedBy
    val createdBy: Long? = null

    @LastModifiedBy
    val lastModifiedBy: Long? = null
     **/

}