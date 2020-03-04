package me.shelves.backend.user.model

import javax.persistence.Embeddable

@Embeddable
class Roles {
    companion object{
        const val ADMIN = "Admin"
        const val GUEST = "Guest"
        const val PREMIUM = "Premium User"
        const val FREE = "Free User"
    }
}