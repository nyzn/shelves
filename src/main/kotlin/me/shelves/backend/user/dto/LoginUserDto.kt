package me.shelves.backend.user.dto

import me.shelves.backend.user.ValidPassword
import javax.validation.constraints.Email

data class LoginUserDto(@Email var email: String,
                        @ValidPassword var password: String) {
}