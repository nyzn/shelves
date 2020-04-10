package me.shelves.backend.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    fun addUser(user: User) : User {
        if(userRepository.existsByEmail(user.email)){
            throw UserAlreadyExistException("User Already exist in the DB")
        } else{
            return userRepository.save(user)
        }
    }

    fun updateUser(dbUser: User, newUser: User) : User {
        dbUser.email = newUser.email
        dbUser.firstName = newUser.firstName
        dbUser.lastName = newUser.lastName
        dbUser.salutation = newUser.salutation
        return userRepository.save(dbUser)
    }

    fun getLoggedInUser(): User? {
        return if(SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().authentication != null)
            userRepository.findByEmail(SecurityContextHolder.getContext().authentication.name)
        else {
            return null
        }
    }

    fun changeUserPassword(user: User, password: String) {
        user.password = passwordEncoder.encode(password)
        userRepository.save(user)
    }

    fun checkIfValidOldPassword(user: User, oldPassword: String): Boolean{
        return passwordEncoder.matches(user.password, oldPassword)
    }

    fun checkIfDifferencePassword(user: User, newPassword: String): Boolean {
        return passwordEncoder.matches(newPassword, user.password)
    }
}