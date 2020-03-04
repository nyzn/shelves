package me.shelves.backend.user.dto

import me.shelves.backend.user.model.Salutation

data class UserDto(var uuid: String,
                   var firstName: String,
                   var lastName: String,
                   var email: String,
                   var salutation: Salutation)