package me.shelves.backend.utile

import org.springframework.http.HttpStatus
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ShelvesBasicAuthenticationEntryPoint: BasicAuthenticationEntryPoint() {

    override fun commence(request: HttpServletRequest?, response: HttpServletResponse?,
                          authException: AuthenticationException?) {
        if (authException is LockedException) {
            //Integer minutesRemaining=this.userService.getRemainingLockedMinutesForCurrent();
            //response.addHeader("Locktime", minutesRemaining.toString());
            response!!.sendError(HttpStatus.LOCKED.value(), authException.localizedMessage)
        } else if (authException is DisabledException) {
            response!!.sendError(HttpStatus.LOCKED.value(), authException.message)
        } else { // Browser Authentifizierungsfenster nur bei swagger anfordern
            val requestURL = request!!.requestURL
            if (requestURL?.toString() != null) {
                val url = requestURL.toString()
                if (url.endsWith("/swagger-ui.html")) {
                    super.commence(request, response, authException)
                } else {
                    response!!.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.reasonPhrase)
                }
            } else {
                response!!.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.reasonPhrase)
            }
        }
    }

    override fun afterPropertiesSet() {
        realmName = "Shelves Application"
        super.afterPropertiesSet()
    }
}
