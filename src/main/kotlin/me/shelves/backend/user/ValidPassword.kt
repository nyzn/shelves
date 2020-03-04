package me.shelves.backend.user

import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PasswordConstraintValidator::class])
annotation class ValidPassword (val message: String = "Invalid password",
                                val groups: Array<KClass<*>> = [],
                                val payload: Array<KClass<out Payload>> = [])

class PasswordConstraintValidator : ConstraintValidator<ValidPassword, String> {

    override fun isValid(password: String, context: ConstraintValidatorContext?): Boolean {
        return password.matches(Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%^&*_=+-]).{8,}\$"))
    }
}