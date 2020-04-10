package me.shelves.backend.user

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import me.shelves.backend.BaseController
import me.shelves.backend.user.dto.CreateUserDto
import me.shelves.backend.user.dto.PasswordDto
import me.shelves.backend.user.dto.UserDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/user")
class UserController (@Autowired var userRepository: UserRepository,
                        @Autowired var userService: UserService) : BaseController<User>(){

    @DeleteMapping("/delete/{uuid}")
    @ApiResponses(
            ApiResponse(code = 400, message = "Die Anfrage wurde falsch aufgebaut"),
            ApiResponse(code =404, message =" User nicht gefunden in der DB."),
            ApiResponse(code =200, message ="Nutzer wurde wurde erfolgreich gelöscht"),
            ApiResponse(code = 500, message = "Dies ist ein „Sammel-Statuscode“ für unerwartete Serverfehler.")
    )
    @ApiOperation(value = "Lösche User anhand seiner Uuid")
    fun delete(@PathVariable uuid: String): ResponseEntity<String> {
        val userFromDB = userRepository.findByUuid(uuid)
        return run {
            userRepository.delete(userFromDB)
            ResponseEntity.ok().build()
        }
    }

    @PostMapping("/changePassword")
    @ApiOperation("Change user password")
    fun changePassword(@Valid @RequestBody passwordDto: PasswordDto): ResponseEntity<Any> {
        val auth: Authentication = SecurityContextHolder.getContext().authentication
        val authUser: User? = userRepository.findByEmail(auth.name) ?: return ResponseEntity.notFound().build()

        if(passwordDto.newPassword.length <= 8) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password is too short.");
        }

        if(authUser?.let { userService.checkIfValidOldPassword(it, passwordDto.oldPassword) }!!) {
            if(userService.checkIfDifferencePassword(authUser, passwordDto.newPassword)) {
                userService.changeUserPassword(authUser, passwordDto.newPassword)
            } else {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).
                        body("The committed new password should not be the same as old password")
            }
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body("The committed old password doesn't match with the password in the system");
        }



        return ResponseEntity.ok().build()
    }

    @PutMapping("/add")
    @ApiOperation(value = "Add new User to the DB")
    fun add(@Valid @RequestBody userDto: CreateUserDto): ResponseEntity<String> {
        return try {
            val user = this.userService.addUser(userDto.toUser())
            ResponseEntity<String>("User created successful ", HttpStatus.CREATED)
        } catch (ex: Exception) {
            log.info("Can't create User: " + ex.message)
            ResponseEntity<String>(ex.message, HttpStatus.BAD_REQUEST)
        }
    }

    @PutMapping("/update")
    @ApiOperation("update user data with specific uuid")
    fun update(@RequestBody user: UserDto): ResponseEntity<String> {
        val userFromDb : User? = userRepository.findByUuid(user.uuid)
        return if(userFromDb != null) {
            userService.updateUser(userFromDb, user.toUser())
            ResponseEntity<String>("Update $user successful ", HttpStatus.OK)
        } else {
            ResponseEntity("Cannot find user: $user", HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/loggedInUser")
    @ApiOperation("Get logged in User")
    @ResponseBody
    fun getCurrent() : UserDto? {
        return userService.getLoggedInUser()?.toUserDto()
    }

    @GetMapping("/all",params = [ "page", "size" ])
    @ApiOperation("get All User")
    fun getAll(@RequestParam("page") page: Int, @RequestParam("size") size: Int)
            : ResponseEntity<Page<User>> {
        val pageUser: Page<User> = userRepository.findAll(PageRequest.of(page,size))

        return ResponseEntity(pageUser, HttpStatus.OK)
    }

    /**
     * Future Features getAll with BaseController
     */
    fun getAllFuture(request: HttpServletRequest, @RequestHeader("ShelvesPaging") pagingDetails: String,
                     @NotNull instanceOfEntity: UserDto)
            : ResponseEntity<Page<User>> {

        return super.getAll(pagingDetails, instanceOfEntity.toUser())
    }


}


/**
 * Convert CreateUserDto to User
 */
private fun CreateUserDto.toUser()= User(
        email = email,
        firstName = firstName,
        lastName = lastName,
        password = password,
        salutation = salutation
)

/**
 * Convert UserDto to User
 */
private fun UserDto.toUser() = User(
        email = email,
        firstName = firstName,
        lastName = lastName,
        salutation = salutation
)

/**
 * Convert User to UserDto
 */
private fun User.toUserDto()= UserDto (
        uuid = uuid,
        firstName =firstName,
        lastName = lastName,
        email = email,
        salutation = salutation
)
