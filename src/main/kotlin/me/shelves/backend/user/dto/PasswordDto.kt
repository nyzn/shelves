package me.shelves.backend.user.dto

data class PasswordDto(var oldPassword: String,
                       var newPassword: String) {
}