package me.shelves.backend.user.dto

import me.shelves.backend.user.model.Salutation
import me.shelves.backend.user.ValidPassword
import javax.validation.constraints.Email

data class CreateUserDto(@Email var email: String,
                         var firstName: String,
                         var lastName: String,
                         @ValidPassword
                         var password: String,
                         var salutation: Salutation) {
}


