package me.shelves.backend.config

import me.shelves.backend.user.CustomUserDetailsService
import me.shelves.backend.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component("authenticationProvider")
class LoginAuthenticationProvider : DaoAuthenticationProvider() {

    @Autowired
    override fun setUserDetailsService(@Qualifier("customUserDetailsService") userDetailsService: UserDetailsService?) {
        super.setUserDetailsService(userDetailsService)
    }

    override fun authenticate(authentication: Authentication?): Authentication {
        try {
            return super.authenticate(authentication)
        } catch (exception : Exception) {
            throw exception

        }
    }
}