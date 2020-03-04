package me.shelves.backend.config

import me.shelves.backend.user.User
import me.shelves.backend.user.UserService
import me.shelves.backend.user.model.RoleType
import me.shelves.backend.user.model.Salutation
import me.shelves.backend.utile.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class Userinitliazer: ApplicationListener<ContextRefreshedEvent> {
    companion object : Log() {}

    @Value("\${init}")
    var init: Boolean = false

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    override fun onApplicationEvent(p0: ContextRefreshedEvent) {
        if (init) {
            var users: HashMap<Int, String> = hashMapOf(0 to "admin", 1 to "guest", 2 to "free", 3 to "premium")
            var roleType = enumValues<RoleType>()
            var salute = arrayOf(Salutation.HERR, Salutation.FRAU, Salutation.UNBEKANNT, Salutation.HERR)
            log.info("--------- Running User Initializer ---------")
            for ((key,value) in users) {
                if(!userService.existByEmail("$value@fake.de")) {
                    //userService.deleteUser(userService.getByEmail("$value@fake.de"))
                    val newUser = User("$value@fake.de", "First name $value",
                            "Last name $value", passwordEncoder.encode("password123"), salute[key])
                    newUser.enable = true
                    newUser.roleType = roleType[key]
                    userService.addUser(newUser)
                }
            }
            log.info("--------- Finished User Initializer ---------")
        }


    }

}