package me.shelves.backend.user

import java.lang.Exception

class UserAlreadyExistException(message: String): Exception(message)

class UserNotFoundException(message: String): Exception(message)

class InvalidJwtAuthenticationException(message: String): Exception(message)

class BadCredentialsException(message: String): Exception(message)