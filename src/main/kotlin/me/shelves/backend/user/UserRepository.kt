package me.shelves.backend.user

import me.shelves.backend.BaseRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: BaseRepository<User> {
    override fun findByUuid(uuid: String): User
    fun findByEmail(email: String): User
    fun existsByEmail(email: String): Boolean
}